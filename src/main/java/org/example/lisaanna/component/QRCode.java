package org.example.lisaanna.component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.example.lisaanna.entity.AppUser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
/*
@Component
public class QRCode {
    private final QRCodeWriter writer = new QRCodeWriter();
    private final static String APP_NAME = "two-factor-app:";
    private final Map<EncodeHintType, ?> hints = Collections.singletonMap(EncodeHintType.ERROR_CORRECTION,
            ErrorCorrectionLevel.M);
    private final MatrixToImageConfig config;
    private final int size;
    public QRCode() {
        this.config = new MatrixToImageConfig();
        this.size = 200;
    }
    public String dataUrl(AppUser user) {
        String otpAuthUrl = "otpauth://totp/%s?secret=%s&digits=6".formatted(APP_NAME + ": " + user.getEmail(),
                user.getSecret());
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            BitMatrix bitMatrix = this.writer.encode(otpAuthUrl, BarcodeFormat.QR_CODE, size, size, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", output, this.config);
            return "data:image/png;base64,%s".formatted(Base64.getEncoder().encodeToString(output.toByteArray()));
        }
        catch (WriterException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

 */
