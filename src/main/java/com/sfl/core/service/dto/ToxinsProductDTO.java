package com.sfl.core.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.Instant;

public class ToxinsProductDTO {
    private Long id;
    private String toxinColor;
    private String CmaToxCsIdMaster;
    private String primaryToxinNamesOrig;
    private String calsaferEffects1;
    private String trichemCancerChronEffects1;
    private String efsaEffects1;
    private String prop65Effects1;
    private String echaSvhcEffects1;
    private String lancetNormanHumanneurotoxEffects1;
    private String japanGhsEffects1;
    private String triChemPfasEffects1;
    private String chemsecEffects1;
    private String epaProdSuspectEffects1;
    private String iarc2024Effects1;
    private String ntpCarcinEffects1;
    private String tedxEffects1;
    private String unepPopsEffects1;
    private String atsdr2022Effects1;
    private String CmaToxCsIdSingle;
    private String idNameSet;
    private String createdBy;
    private Instant createdDate = Instant.now();
    private String lastModifiedBy;
    private Instant lastModifiedDate = Instant.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToxinColor() {
        return toxinColor;
    }

    public void setToxinColor(String toxinColor) {
        this.toxinColor = toxinColor;
    }

    public String getCmaToxCsIdMaster() {
        return CmaToxCsIdMaster;
    }

    public void setCmaToxCsIdMaster(String cmaToxCsIdMaster) {
        CmaToxCsIdMaster = cmaToxCsIdMaster;
    }

    public String getPrimaryToxinNamesOrig() {
        return primaryToxinNamesOrig;
    }

    public void setPrimaryToxinNamesOrig(String primaryToxinNamesOrig) {
        this.primaryToxinNamesOrig = primaryToxinNamesOrig;
    }

    public String getCalsaferEffects1() {
        return calsaferEffects1;
    }

    public void setCalsaferEffects1(String calsaferEffects1) {
        this.calsaferEffects1 = calsaferEffects1;
    }

    public String getTrichemCancerChronEffects1() {
        return trichemCancerChronEffects1;
    }

    public void setTrichemCancerChronEffects1(String trichemCancerChronEffects1) {
        this.trichemCancerChronEffects1 = trichemCancerChronEffects1;
    }

    public String getEfsaEffects1() {
        return efsaEffects1;
    }

    public void setEfsaEffects1(String efsaEffects1) {
        this.efsaEffects1 = efsaEffects1;
    }

    public String getProp65Effects1() {
        return prop65Effects1;
    }

    public void setProp65Effects1(String prop65Effects1) {
        this.prop65Effects1 = prop65Effects1;
    }

    public String getEchaSvhcEffects1() {
        return echaSvhcEffects1;
    }

    public void setEchaSvhcEffects1(String echaSvhcEffects1) {
        this.echaSvhcEffects1 = echaSvhcEffects1;
    }

    public String getLancetNormanHumanneurotoxEffects1() {
        return lancetNormanHumanneurotoxEffects1;
    }

    public void setLancetNormanHumanneurotoxEffects1(String lancetNormanHumanneurotoxEffects1) {
        this.lancetNormanHumanneurotoxEffects1 = lancetNormanHumanneurotoxEffects1;
    }

    public String getJapanGhsEffects1() {
        return japanGhsEffects1;
    }

    public void setJapanGhsEffects1(String japanGhsEffects1) {
        this.japanGhsEffects1 = japanGhsEffects1;
    }

    public String getTriChemPfasEffects1() {
        return triChemPfasEffects1;
    }

    public void setTriChemPfasEffects1(String triChemPfasEffects1) {
        this.triChemPfasEffects1 = triChemPfasEffects1;
    }

    public String getChemsecEffects1() {
        return chemsecEffects1;
    }

    public void setChemsecEffects1(String chemsecEffects1) {
        this.chemsecEffects1 = chemsecEffects1;
    }

    public String getEpaProdSuspectEffects1() {
        return epaProdSuspectEffects1;
    }

    public void setEpaProdSuspectEffects1(String epaProdSuspectEffects1) {
        this.epaProdSuspectEffects1 = epaProdSuspectEffects1;
    }

    public String getIarc2024Effects1() {
        return iarc2024Effects1;
    }

    public void setIarc2024Effects1(String iarc2024Effects1) {
        this.iarc2024Effects1 = iarc2024Effects1;
    }

    public String getNtpCarcinEffects1() {
        return ntpCarcinEffects1;
    }

    public void setNtpCarcinEffects1(String ntpCarcinEffects1) {
        this.ntpCarcinEffects1 = ntpCarcinEffects1;
    }

    public String getTedxEffects1() {
        return tedxEffects1;
    }

    public void setTedxEffects1(String tedxEffects1) {
        this.tedxEffects1 = tedxEffects1;
    }

    public String getUnepPopsEffects1() {
        return unepPopsEffects1;
    }

    public void setUnepPopsEffects1(String unepPopsEffects1) {
        this.unepPopsEffects1 = unepPopsEffects1;
    }

    public String getAtsdr2022Effects1() {
        return atsdr2022Effects1;
    }

    public void setAtsdr2022Effects1(String atsdr2022Effects1) {
        this.atsdr2022Effects1 = atsdr2022Effects1;
    }

    public String getCmaToxCsIdSingle() {
        return CmaToxCsIdSingle;
    }

    public void setCmaToxCsIdSingle(String cmaToxCsIdSingle) {
        CmaToxCsIdSingle = cmaToxCsIdSingle;
    }

    public String getIdNameSet() {
        return idNameSet;
    }

    public void setIdNameSet(String idNameSet) {
        this.idNameSet = idNameSet;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
