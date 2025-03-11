package com.example.qr_code_generator.service;


import com.example.qr_code_generator.utils.QRCodeDecoder;
import com.example.qr_code_generator.utils.QRCodeGenerator;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class QRCodeService {

    public byte[] generateQRCode(String text) throws WriterException, IOException {
        return QRCodeGenerator.generateQRCodeImage(text, 300, 300);
    }
    public String verifyQRCode(MultipartFile file) throws IOException, NotFoundException {
        return QRCodeDecoder.decodeQRCode(file.getInputStream());
    }
}
