package com.example.qr_code_generator.controller;
import com.example.qr_code_generator.service.QRCodeService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin(origins = "http://localhost:5173")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String link) {
        try {
            byte[] qrCode = qrCodeService.generateQRCode(link);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                    .body(qrCode);
        } catch (WriterException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyQRCode(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            response.put("valid", true);
            response.put("text", result.getText()); // Extracted QR content
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", "Invalid QR Code");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}

