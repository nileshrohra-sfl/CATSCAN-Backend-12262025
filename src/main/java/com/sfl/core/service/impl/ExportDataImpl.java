package com.sfl.core.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.domain.TryAgainProduct;
import com.sfl.core.repository.FavoriteProductRepository;
import com.sfl.core.repository.GenericReportRepository;
import com.sfl.core.repository.SflUserExtendRepository;
import com.sfl.core.service.ExportData;
import com.sfl.core.service.dto.RequestReportDTO;
import com.sfl.core.service.dto.TryAgainProductResultJsonData;
import com.sfl.core.service.enums.ExportFileType;
import com.sfl.core.service.mapper.CommonAfterMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.sfl.core.repository.constants.CacheConstants.TABLE_EXTENDED_USER;


/**
 * This service responsible for exporting data with different format
 *
 * @author Henil Mistry
 * Start FROM CMA-282
 */
@Service
public class ExportDataImpl implements ExportData {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final GenericReportRepository genericReportRepository;

    private final SflUserExtendRepository sflUserExtendRepository;

    private final CommonAfterMapper commonAfterMapper;

    private final FavoriteProductRepository favouriteProductRepository;
    private static final String CHOICE_PREFIX = "choice_";
    private static final String PROD_ID = "_prod_id";
    private static final String PROD_NAME = "_prod_name";
    private static final String TOKENS = "_tokens";
    private static final String MATCHING_TOKENS = "_tokens_matched";
    private static final String DISTANCE_MATCHING_TOKENS = "_tokens_levdist";
    private static final String SCORE = "_score";
    private static final String CREATED_BY = "mo_user_mobile";
    private static final String CREATED_AT = "mo_prod_scan_date";
    private static final String ID = "mo_prod_scan_id";
    private static final String MO_PROD_S3_URI = "mo_prod_s3_uri";
    private static final String RAW_OCR_OUTPUT = "mo_prod_scan_raw_ocr";
    private static final String OCR_RESULTS = "mo_prod_scan_ocr_tokens";
    private static final String SESSION_ID = "mo_prod_scan_batch_id";
    private static final String USER_SELECTION_ID = "mo_user_prod_select_id";
    private static final String USER_SELECTION_NUMBER = "mo_user_prod_selection";
    private static final String USER_SELECTION_NAME = "mo_user_prod_select_name";
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    private static final String COL_USER_ID = "mo_user_acct_id";
    private static final String COL_USER_MOBILE = "mo_user_mobile";
    private static final String COL_FIRST_NAME = "mo_user_first_name";
    private static final String COL_LAST_NAME = "mo_user_last_name";
    private static final String COL_EMAIL = "mo_user_email";
    private static final String COL_FAV_PROD_ID = "mo_user_fav_prod_id";
    private static final String COL_LAST_CHANGE_DATE = "mo_user_last_change_date";
    private static final String COL_STATUS = "mo_user_status";
    private static final String COL_CREATED_DATE = "mo_user_acct_created_date";
    private static final String COL_DELETED_DATE = "mo_user_acct_deleted_date";
    private static final String ALLERGEN_PREFIX = "mo_user_alg_";


    public ExportDataImpl(GenericReportRepository genericReportRepository, SflUserExtendRepository sflUserExtendRepository, CommonAfterMapper commonAfterMapper, FavoriteProductRepository favouriteProductRepository) {
        this.genericReportRepository = genericReportRepository;
        this.sflUserExtendRepository = sflUserExtendRepository;
        this.commonAfterMapper = commonAfterMapper;
        this.favouriteProductRepository = favouriteProductRepository;
    }

    /**
     * Based on file type call writer implement.
     * and export the data
     *
     * @param requestReportDTO
     * @param response
     */
    @Override
    public void exportGenericData(RequestReportDTO requestReportDTO, HttpServletResponse response) {
        log.info("Start Export Process.");
        ExportFileType exportFileType = ObjectUtils.defaultIfNull(requestReportDTO.getExportFileType(), ExportFileType.EXCEL);
        if (Objects.requireNonNull(exportFileType) == ExportFileType.EXCEL) {
            excelFileWriter(requestReportDTO, response);
        } else {
            log.warn("Export file type not supported. {}", exportFileType);
            throw new IllegalStateException("Unexpected export type: " + exportFileType);
        }
    }

    @Override
    public void exportTryAgainProductData(RequestReportDTO requestReportDTO, HttpServletResponse response) {
        log.info("Start export process for try-again products.");
        ExportFileType exportFileType = ObjectUtils.defaultIfNull(requestReportDTO.getExportFileType(), ExportFileType.EXCEL);
        if (Objects.requireNonNull(exportFileType) == ExportFileType.EXCEL) {
            tryAgainExcelFileWriter(requestReportDTO, response);
        } else {
            log.warn("Export file type not supported. {}", exportFileType);
            throw new IllegalStateException("Unexpected export type: " + exportFileType);
        }
    }

    /**
     * Write and export data in Excel format.
     *
     * @param requestReportDTO
     * @param response
     */
    private void tryAgainExcelFileWriter(RequestReportDTO requestReportDTO, HttpServletResponse response) {
        log.info("Start writing try-again product data to Excel.");
        response.setContentType(CONTENT_TYPE);
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename= user_ocr_selection.xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("user_ocr_selection");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // First pass: Fetch all records and determine max number of choices
            int maxChoices = 0;
            List<TryAgainProduct> allRecords = new ArrayList<>();
            Pageable pageable = PageRequest.of(0, 1000);
            Page<TryAgainProduct> page;

            do {
                page = genericReportRepository.fetchTryAgainProducts(requestReportDTO, pageable);
                for (TryAgainProduct tryAgainProduct : page.getContent()) {
                    allRecords.add(tryAgainProduct);
                    if (tryAgainProduct.getOcrResult() != null) {
                        maxChoices = Math.max(maxChoices, tryAgainProduct.getOcrResult().size());
                    }
                }
                pageable = pageable.next();
            } while (page.hasNext());

            // Create dynamic headers
            List<String> headers = new ArrayList<>(Arrays.asList(
                MO_PROD_S3_URI, RAW_OCR_OUTPUT, OCR_RESULTS, USER_SELECTION_NUMBER, USER_SELECTION_ID, USER_SELECTION_NAME
            ));
            for (int i = 1; i <= maxChoices; i++) {
                headers.add(CHOICE_PREFIX + i + PROD_ID);
                headers.add(CHOICE_PREFIX + i + PROD_NAME);
                headers.add(CHOICE_PREFIX + i + TOKENS);
                headers.add(CHOICE_PREFIX + i + MATCHING_TOKENS);
                headers.add(CHOICE_PREFIX + i + DISTANCE_MATCHING_TOKENS);
                headers.add(CHOICE_PREFIX + i + SCORE);
            }
            headers.add(COL_USER_ID);
            headers.add(CREATED_BY);
            headers.add(CREATED_AT);
            headers.add(SESSION_ID);
            headers.add(ID);

            // Write headers to Excel
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            // Write data rows
            writeDataRows(sheet, allRecords, maxChoices, mapper);
            // Write workbook to response
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("Failed to generate Excel report: ", e);
        }
    }

    /**
     * Write data rows to the Excel sheet.
     */
    private void writeDataRows(Sheet sheet, List<TryAgainProduct> records, int maxChoices, ObjectMapper mapper) {
        log.info("Writing data rows to Excel.");
        int rowNum = 1;

        for (TryAgainProduct product : records) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;

            row.createCell(colNum++).setCellValue(cleanValue(commonAfterMapper.getFullUrl(product.getImage())));
            row.createCell(colNum++).setCellValue(cleanValue(product.getRawOcr()));
            row.createCell(colNum++).setCellValue(cleanValue(product.getOriginalOcrResult()));
            String number = "";
            String name = "";
            if (Objects.nonNull(product.getUserSelection())) {
                String userSelection = product.getUserSelection();
                if (userSelection.contains("] :")) {
                    String[] parts = userSelection.split("] :", 2);
                    if (parts.length == 2) {
                        number = parts[0].replace("[", "").trim();
                        name = parts[1].trim();
                        row.createCell(colNum++).setCellValue(cleanValue(number));
                        Long prodId = product.getProdId();
                        row.createCell(colNum++).setCellValue(prodId != null && prodId != 0L ? prodId.toString() : "");
                        row.createCell(colNum++).setCellValue(cleanValue(name));
                    }
                } else {
                    // Fallback for plain string like "TryAgain"
                    row.createCell(colNum++).setCellValue(cleanValue(product.getUserSelection()));
                    Long prodId = product.getProdId();
                    row.createCell(colNum++).setCellValue(prodId != null && prodId != 0L ? prodId.toString() : "");
                    row.createCell(colNum++).setCellValue(cleanValue(product.getProdName()));
                }
            }

            List<TryAgainProductResultJsonData> ocrResult = product.getOcrResult();
            for (int i = 0; i < maxChoices; i++) {
                if (ocrResult != null && i < ocrResult.size()) {
                    TryAgainProductResultJsonData result = mapper.convertValue(ocrResult.get(i), TryAgainProductResultJsonData.class);
                    row.createCell(colNum++).setCellValue(result.getProdId() != null ? result.getProdId() : null);
                    row.createCell(colNum++).setCellValue(cleanValue(result.getProdName()));
                    row.createCell(colNum++).setCellValue(cleanValue(result.getOcrToken()));
                    row.createCell(colNum++).setCellValue(cleanValue(result.getExactMatches()));
                    row.createCell(colNum++).setCellValue(cleanValue(result.getDistanceMatches()));
                    row.createCell(colNum++).setCellValue(cleanValue(result.getMatchScores()));
                } else {
                    colNum += 6;
                }
            }
            row.createCell(colNum++).setCellValue(product.getUser() != null ? product.getUser().getId() : 0);
            row.createCell(colNum++).setCellValue(cleanValue(product.getCreatedBy()));
            row.createCell(colNum++).setCellValue(product.getCreatedDate().toString());
            row.createCell(colNum++).setCellValue(cleanValue(product.getSessionId()));
            row.createCell(colNum).setCellValue(product.getId());
        }
    }

    // Helper method to avoid null pointer exceptions
    private String cleanValue(Object value) {
        return value == null ? "" : value.toString();
    }

    /**
     * Write and export data in Excel format.
     *
     * @param requestReportDTO
     * @param response
     */
    public void excelFileWriter(RequestReportDTO requestReportDTO, HttpServletResponse response) {
        log.info("Start writing generic report data to Excel.");
        response.setContentType(CONTENT_TYPE);
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + requestReportDTO.getTableName() + "_report.xlsx");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            // Make a mutable copy of incoming headers — do not mutate the DTO directly
            List<String> headers = new ArrayList<>(requestReportDTO.getHeaderFields());

            // Only add/position the favourite-product column for extended-user table
            if (TABLE_EXTENDED_USER.equalsIgnoreCase(requestReportDTO.getTableName())) {
                // Ensure the header list contains COL_FAV_PROD_ID positioned after COL_EMAIL
                if (headers.stream().anyMatch(COL_FAV_PROD_ID::equalsIgnoreCase) || true) {
                    // Remove any existing fav header to allow re-insert at desired position
                    int existingFavIdx = -1;
                    for (int i = 0; i < headers.size(); i++) {
                        if (headers.get(i) != null && headers.get(i).equalsIgnoreCase(COL_FAV_PROD_ID)) {
                            existingFavIdx = i;
                            break;
                        }
                    }
                    if (existingFavIdx >= 0) {
                        headers.remove(existingFavIdx);
                    }

                    int emailIdx = -1;
                    for (int i = 0; i < headers.size(); i++) {
                        if (headers.get(i) != null && headers.get(i).equalsIgnoreCase(COL_EMAIL)) {
                            emailIdx = i;
                            break;
                        }
                    }

                    int insertAt = (emailIdx >= 0) ? emailIdx + 1 : headers.size();
                    headers.add(Math.min(insertAt, headers.size()), COL_FAV_PROD_ID);
                    log.info("Ensured '{}' header is at index {} for table {}.", COL_FAV_PROD_ID, insertAt, requestReportDTO.getTableName());
                }
            } else {
                // For non-extended-user tables, do nothing — keep headers as provided
                log.debug("Table '{}' is not '{}'; favourite-product column will not be added.", requestReportDTO.getTableName(), TABLE_EXTENDED_USER);
            }

            Sheet sheet = workbook.createSheet("Report");

            int rowIdx = 0;
            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }

            Pageable pageable = PageRequest.of(0, 1000);
            Page<Object[]> page;

            do {
                page = genericReportRepository.fetchReportData(requestReportDTO, pageable);
                for (Object[] record : page) {
                    Row row = sheet.createRow(rowIdx++);

                    int recIdx = 0;
                    for (int col = 0; col < headers.size(); col++) {
                        String header = headers.get(col);
                        Cell cell = row.createCell(col);

                        // Only for extended-user, handle the computed fav column
                        if (TABLE_EXTENDED_USER.equalsIgnoreCase(requestReportDTO.getTableName())
                            && header != null
                            && header.equalsIgnoreCase(COL_FAV_PROD_ID)) {

                            String favCellValue = "";
                            try {
                                // find user id field index using heuristics (same as before)
                                int userIdFieldIndex = -1;
                                List<String> fields = requestReportDTO.getFields();
                                for (int i = 0; i < fields.size(); i++) {
                                    String f = fields.get(i);
                                    if (f == null) continue;
                                    String lower = f.toLowerCase();
                                    if (lower.equals("id") || lower.equals("userid") || lower.equals("user_id") || lower.endsWith(".id")
                                        || (lower.contains("user") && lower.contains("id"))) {
                                        userIdFieldIndex = i;
                                        break;
                                    }
                                }

                                if (userIdFieldIndex >= 0 && userIdFieldIndex < record.length && record[userIdFieldIndex] != null) {
                                    Object userIdObj = record[userIdFieldIndex];
                                    long userId;
                                    if (userIdObj instanceof Number) {
                                        userId = ((Number) userIdObj).longValue();
                                    } else {
                                        userId = Long.parseLong(userIdObj.toString());
                                    }

                                    // Helper call — ensure your repository has this method or rename accordingly
                                    List<Long> favIds = favouriteProductRepository.findProdIdsByUserId(userId);
                                    if (favIds != null && !favIds.isEmpty()) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append('[');
                                        for (int k = 0; k < favIds.size(); k++) {
                                            if (k > 0) sb.append(',');
                                            sb.append(favIds.get(k));
                                        }
                                        sb.append(']');
                                        favCellValue = sb.toString();
                                    }
                                } else {
                                    log.debug("UserId not found in fields/record; fav value left empty for this row.");
                                }
                            } catch (Exception e) {
                                log.error("Error while computing favourite products for row: {}", e.getMessage());
                            }
                            cell.setCellValue(favCellValue);

                            // Do NOT advance recIdx because fav column is not part of DB record[]
                        } else {
                            // Normal header column — take the next DB value in order
                            String value = "";
                            if (recIdx < record.length && record[recIdx] != null) {
                                value = record[recIdx].toString();
                            }
                            cell.setCellValue(value);
                            recIdx++;
                        }
                    }
                }
                pageable = pageable.next();
            } while (page.hasNext());

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            log.error("Excel export failed", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }



    private void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    private String escapeCsvValue(Object value) {
        if (value == null) {
            return ""; // Handle null values
        }
        String stringValue = value.toString();
        // Check if the value contains a comma or double quote
        if (stringValue.contains(",") || stringValue.contains("\"")) {
            // Escape double quotes by doubling them and wrap the value in double quotes
            stringValue = "\"" + stringValue.replace("\"", "\"\"") + "\"";
        }
        return stringValue;
    }

    /**
     * Generate Excel file for mobile user data.
     *
     * @param response
     */
    public void exportMobileUserData(HttpServletResponse response) {
        log.info("Start writing mobile user data to Excel.");
        List<SflUserExtend> sflUserExtendList = sflUserExtendRepository.findAllByIsDeleted(Boolean.FALSE);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("mobile_users");

            // Step 1: Collect and sort allergens
            Map<String, String> sortedAllergenMap = generateSortedAllergenMap(sflUserExtendList);

            final int ALLERGEN_INSERT_INDEX = 5;
            List<String> columns = new ArrayList<>(List.of(COL_USER_ID, COL_USER_MOBILE, COL_FIRST_NAME, COL_LAST_NAME, COL_EMAIL, COL_LAST_CHANGE_DATE, COL_STATUS, COL_CREATED_DATE, COL_DELETED_DATE));
            columns.addAll(ALLERGEN_INSERT_INDEX, new ArrayList<>(sortedAllergenMap.values()));

            createHeaderRow(sheet, columns);

            for (int i = 0; i < sflUserExtendList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                writeUserRow(row, sflUserExtendList.get(i), sortedAllergenMap);
            }

            response.setContentType(CONTENT_TYPE);
            response.setHeader(CONTENT_DISPOSITION, "attachment; filename=mobile_users.xlsx");
            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            log.error("Excel export failed", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Generate a sorted map of allergens from the list of users.
     *
     * @param users List of SflUserExtend objects
     * @return Sorted map of allergens
     */
    public Map<String, String> generateSortedAllergenMap(List<SflUserExtend> users) {
        log.info("Generating sorted allergen map.");
        return users.stream()
            .filter(user -> user.getAllergens() != null)
            .flatMap(user -> user.getAllergens().keySet().stream())
            .distinct()
            .collect(Collectors.toMap(
                key -> key,
                key -> ALLERGEN_PREFIX + key.toLowerCase().replaceAll("\\s+", "_"),
                (a, b) -> a,
                LinkedHashMap::new
            ))
            .entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
            ));
    }

    /**
     * Write user data to the specified row.
     *
     * @param row               The row to write to
     * @param user              The user data
     * @param sortedAllergenMap The sorted allergen map
     */
    private void writeUserRow(Row row, SflUserExtend user, Map<String, String> sortedAllergenMap) {
        log.info("Writing user data to row.");
        row.createCell(0).setCellValue(user.getId() != null ? user.getId() : 0);
        row.createCell(1).setCellValue(user.getPhoneNumber() != null ? user.getPhoneNumber().toString() : "");
        row.createCell(2).setCellValue(user.getFirstName() != null ? user.getFirstName() : "");
        row.createCell(3).setCellValue(user.getLastName() != null ? user.getLastName() : "");
        row.createCell(4).setCellValue(user.getEmail() != null ? user.getEmail() : "");

        Map<String, Object> allergens = user.getAllergens();
        int cellIndex = 5;
        for (String originalKey : sortedAllergenMap.keySet()) {
            Object allergenData = allergens != null ? allergens.get(originalKey) : null;
            row.createCell(cellIndex++).setCellValue(getAllergenValue(allergenData));
        }

        int lastIndex = 5 + sortedAllergenMap.size();
        row.createCell(lastIndex).setCellValue(user.getLastModifiedDate() != null ? user.getLastModifiedDate().toString() : "");
        String statusText = Boolean.TRUE.equals(user.getStatus()) ? "active" : "deleted";
        row.createCell(lastIndex + 1).setCellValue(statusText);
        row.createCell(lastIndex + 2).setCellValue(user.getCreatedDate() != null ? user.getCreatedDate().toString() : "");
        row.createCell(lastIndex + 3).setCellValue(user.getDeletedDate() != null ? user.getDeletedDate().toString() : "");
    }

    /**
     * Create header row in the Excel sheet.
     *
     * @param sheet   The Excel sheet
     * @param columns The list of column names
     */
    private void createHeaderRow(Sheet sheet, List<String> columns) {
        log.info("Creating header row in Excel sheet.");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i));
        }
    }

    /**
     * Get allergen value based on the data type.
     *
     * @param allergenData The allergen data
     * @return The allergen value as a string
     */
    public String getAllergenValue(Object allergenData) {
        log.info("Get allergen value");
        if (allergenData instanceof Map<?, ?> flags) {
            boolean isNotify = Boolean.TRUE.equals(flags.get("isNotify"));
            boolean isRedZone = Boolean.TRUE.equals(flags.get("isRedZone"));
            if (isRedZone) return "redzone";
            if (isNotify) return "notify";
        }
        return "none";
    }
}
