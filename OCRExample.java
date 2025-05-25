import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRExample {
    public static void main(String[] args) {
        Tesseract tesseract = new Tesseract();

        // Nếu bạn cài ngôn ngữ tiếng Việt, set path tessdata và language
        // tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata/"); // Ví dụ trên Ubuntu
        // tesseract.setLanguage("vie");

        try {
            File imageFile = new File("code.png"); // Đường dẫn ảnh
            String result = tesseract.doOCR(imageFile);
            System.out.println("Kết quả OCR: ");
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
