package com.ooops.lms.barcode;

public class Vinhcc {
    public static void main(String[] args) {
        System.out.println("Vinhcc");
        BarcodeScanner scanner = new BarcodeScanner();
        scanner.scanBarcodeFromCamera();
        System.out.println("Vinhccc");
    }
}
