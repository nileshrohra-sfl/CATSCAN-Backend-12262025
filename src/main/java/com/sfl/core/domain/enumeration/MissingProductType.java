package com.sfl.core.domain.enumeration;

public enum MissingProductType {
    MISSING("Missing or Misidentified Product"),
    INGREDIENT_ERROR("Ingredient Error"), OTHER("Other Product Error"),
    BARCODE_ERROR("Barcode Type Error"), MISSING_BARCODE("Missing Barcode");

    String value;

    MissingProductType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
