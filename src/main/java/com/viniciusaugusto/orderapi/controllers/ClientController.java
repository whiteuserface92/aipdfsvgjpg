package com.viniciusaugusto.orderapi.controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.dom.GenericDOMImplementation;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderException;

import org.json.JSONObject;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.UUID;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

// import com.aspose.psd.Image;
// import com.aspose.psd.imageoptions.SvgOptions;

@RestController
@RequestMapping(value = "/test", produces = "application/json")
public class ClientController {

    String uploadFolder = "C:/Users/paru/downloads/";

    @PostMapping
    @RequestMapping("/getTestdata")
    public String getTestdata(@RequestParam("file") MultipartFile file) {



        if (file.isEmpty()) {
            return "No file uploaded.";
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            return "File name is empty.";
        }

        return "File uploaded successfully. File name: " + fileName;
    }

    @PostMapping("/convertAiToJpgVone")
    public String convertAiToJpg(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        long fileSize = file.getSize();

        System.out.println("fileSize : "+fileSize);
        
        long startTime = System.currentTimeMillis(); // 실행 시간 측정 시작

        String fileAbsolutePath = uploadFolder;
        String fileName = file.getOriginalFilename();
        String fullFile = fileAbsolutePath+fileName;

        // AI 파일을 PDF로 변환하는 외부 툴을 호출 (예: Inkscape 또는 ImageMagick)
        long convertAiToPdfstartTime = System.currentTimeMillis();

        //확장자를 변경하고, 복사하여 저장하는 로직 - (사용)
        String pdfFilePath = copyFileWithNewExtension(fileAbsolutePath+file.getOriginalFilename(), fileAbsolutePath, "pdf");

        // 확장자만 변경하여 pdf로 만드는 로직 - (비사용용)
        // String pdfFilePath = changeFileExtension(fullFile, "pdf");
 
        long convertAiToPdfendTime = System.currentTimeMillis();

        File pdfFile = new File(pdfFilePath);

        long pdfToJpgstartTime = System.currentTimeMillis();
        // PDF 파일을 읽기 위해 PDDocument로 로드
        PDDocument document = PDDocument.load(new java.io.File(pdfFilePath));

        // PDFRenderer를 사용하여 PDF 페이지를 이미지로 렌더링
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // 첫 번째 페이지를 이미지로 변환
        BufferedImage bufferedImage = pdfRenderer.renderImage(0, 1.0f, ImageType.RGB);

        String jpgFilePath = pdfFilePath.replace(".pdf","_before.jpg");

        // 이미지 저장
        File outputFile = new File(jpgFilePath);

        // JPG 생성
        ImageIO.write(bufferedImage, "JPG", outputFile);

        // PDF 문서 닫기
        document.close();

        long pdfToJpgendTime = System.currentTimeMillis();

        // 실행 시간 측정 종료 (전체)
        long endTime = System.currentTimeMillis();


        // 실행 시간 계산
        long executionTime = endTime - startTime; 
        long convertAiToPdfexecutionTime = convertAiToPdfendTime - convertAiToPdfstartTime;
        long pdfToJpgexecutionTime = pdfToJpgendTime - pdfToJpgstartTime;

        // JSON 객체로 결과 반환
        JSONObject responseJson = new JSONObject();
        responseJson.put("All executionTime", executionTime / 1000); // 실행 시간
        responseJson.put("convertAiToPdf executionTime", convertAiToPdfexecutionTime / 1000); // convertAiToPdf
        responseJson.put("pdfToJpg executionTime", pdfToJpgexecutionTime / 1000); // convertAiToPdf

        // JSON 객체를 문자열로 반환
        return responseJson.toString();
    }

    @PostMapping("/convertAiToJpgVtwo")
    public String convertAiToJpgVtwo(@RequestParam("filetwo") MultipartFile file) throws IOException, InterruptedException {
        long fileSize = file.getSize();

        System.out.println("fileSize : "+fileSize);
        
        long startTime = System.currentTimeMillis(); // 실행 시간 측정 시작

        String fileAbsolutePath = uploadFolder;
        String fileName = file.getOriginalFilename();
        String fullFile = fileAbsolutePath+fileName;

        // AI 파일을 PDF로 변환하는 외부 툴을 호출 (예: Inkscape 또는 ImageMagick)
        long convertAiToPdfstartTime = System.currentTimeMillis();

        //확장자를 변경하고, 복사하여 저장하는 로직 - (사용)
        // String pdfFilePath = copyFileWithNewExtension(fileAbsolutePath+file.getOriginalFilename(), fileAbsolutePath, "pdf");

        // 확장자만 변경하여 pdf로 만드는 로직 - (비사용용)
        String pdfFilePath = changeFileExtension(fullFile, "pdf");
 
        long convertAiToPdfendTime = System.currentTimeMillis();

        File pdfFile = new File(pdfFilePath);

        long pdfToJpgstartTime = System.currentTimeMillis();
        // PDF 파일을 읽기 위해 PDDocument로 로드
        PDDocument document = PDDocument.load(new java.io.File(pdfFilePath));

        // PDFRenderer를 사용하여 PDF 페이지를 이미지로 렌더링
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // 첫 번째 페이지를 이미지로 변환
        BufferedImage bufferedImage = pdfRenderer.renderImage(0, 1.0f, ImageType.RGB);

        // 이미지 저장
        File outputFile = new File(uploadFolder+"output_image.jpg");

        // JPG 생성
        ImageIO.write(bufferedImage, "JPG", outputFile);

        // PDF 문서 닫기
        document.close();

        long pdfToJpgendTime = System.currentTimeMillis();

        // 실행 시간 측정 종료
        long endTime = System.currentTimeMillis();


        // 실행 시간 계산
        long executionTime = endTime - startTime; 
        long convertAiToPdfexecutionTime = convertAiToPdfendTime - convertAiToPdfstartTime;
        long pdfToJpgexecutionTime = pdfToJpgendTime - pdfToJpgstartTime;

        // JSON 객체로 결과 반환
        JSONObject responseJson = new JSONObject();
        responseJson.put("All executionTime", executionTime); // 실행 시간
        responseJson.put("convertAiToPdf executionTime", convertAiToPdfexecutionTime); // convertAiToPdf
        responseJson.put("pdfToJpg executionTime", pdfToJpgexecutionTime); // convertAiToPdf
        responseJson.put("imageData", outputFile); // JPG 바이트 데이터

        // JSON 객체를 문자열로 반환
        return responseJson.toString();
    }

    @PostMapping("/convertAiToPng")
    public String convertAiToSvgtoJpg(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        long fileSize = file.getSize();

        System.out.println("fileSize : "+fileSize);
        
        long startTime = System.currentTimeMillis(); // 실행 시간 측정 시작

        String fileAbsolutePath = uploadFolder;
        String fileName = file.getOriginalFilename();
        String fullFile = fileAbsolutePath+fileName;

        // AI 파일을 PDF로 변환하는 외부 툴을 호출 (예: Inkscape 또는 ImageMagick)
        long convertAiToPdfstartTime = System.currentTimeMillis();

        //확장자를 변경하고, 복사하여 저장하는 로직 - (사용)
        String pdfFilePath = copyFileWithNewExtension(fileAbsolutePath+file.getOriginalFilename(), fileAbsolutePath, "pdf");

        // 확장자만 변경하여 pdf로 만드는 로직 - (비사용용)
        // String pdfFilePath = changeFileExtension(fullFile, "pdf");
 
        long convertAiToPdfendTime = System.currentTimeMillis();

        // File pdfFile = new File(pdfFilePath);

        long pdfToPngStartTime = System.currentTimeMillis();
        // PDF 파일을 읽기 위해 PDDocument로 로드

        String pngFilePath = pdfFilePath.replace(".pdf",".png");
        
        convertPdfToPng(pdfFilePath, pngFilePath);

        long pdfToPngEndTime = System.currentTimeMillis();

        long pngToJpgStartTime = System.currentTimeMillis();

        //PNG -> JPG 이름변경 -> 형식변경 방법으로 변경경
        String jpgFilePath = pngFilePath.replace(".png",".jpg");
        
        jpgFilePath = convertPngToJpg(pngFilePath, jpgFilePath);

        long pngToJpgEndTime = System.currentTimeMillis();



        // 실행 시간 측정 종료 (전체)
        long endTime = System.currentTimeMillis();


        // 실행 시간 계산
        long executionTime = endTime - startTime; 
        long convertAiToPdfexecutionTime = convertAiToPdfendTime - convertAiToPdfstartTime;
        long pdfToPngExecutionTime = pdfToPngEndTime - pdfToPngStartTime;
        long pngToJpgExecutionTime = pngToJpgEndTime - pngToJpgStartTime;

        // JSON 객체로 결과 반환
        JSONObject responseJson = new JSONObject();
        responseJson.put("All executionTime", executionTime / 1000); // 실행 시간
        responseJson.put("convertAiToPdf executionTime", convertAiToPdfexecutionTime / 1000); // convertAiToPdf
        responseJson.put("pdfToPng ExecutionTime", pdfToPngExecutionTime / 1000); // convertpdfTopng
        responseJson.put("pngToJpg ExecutionTime", pngToJpgExecutionTime / 1000); // PNG -> JPG 변환시간

        
        responseJson.put("jpgFilePath", jpgFilePath); // jpgFilePath

        // JSON 객체를 문자열로 반환
        return responseJson.toString();
    }

    @PostMapping("/ocrJpgImageTest")
    public String convertAiToSvgtoJpg(@RequestParam("imageFileName") String imageFileName) throws IOException, InterruptedException {

        String result = ocrReturnString(imageFileName);

        JSONObject responseJson = new JSONObject();

        responseJson.put("Result : ",result);
        
        return responseJson.toString();
    }


    // JPG 파일을 특정 폴더에 저장하는 메소드
    public void saveJpgFile(MultipartFile file, String targetFolder) throws IOException {
        // 파일의 원래 이름을 가져옴
        String originalFileName = file.getOriginalFilename();
        
        // 파일 확장자가 JPG인지 확인
        if (originalFileName != null && originalFileName.endsWith(".jpg")) {
            // 대상 폴더에 저장할 파일 경로 생성
            Path targetLocation = new File(targetFolder, originalFileName).toPath();

            // 파일을 지정된 폴더로 저장
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("JPG 파일이 성공적으로 저장되었습니다: " + targetLocation.toString());
        } else {
            System.out.println("업로드된 파일이 JPG 파일이 아닙니다.");
        }
    }

     // AI 파일을 PDF로 변환하는 메서드 (예: Inkscape, ImageMagick 사용)
    //  public String convertAiToPdf(MultipartFile file, String fileSaveLocation) throws IOException {
    //     // Save the uploaded AI file to the specified location
    //     String aiFilePath = fileSaveLocation + File.separator + file.getOriginalFilename();
    //     File aiFile = new File(aiFilePath);
    //     file.transferTo(aiFile); // AI파일 저장
    
    //     if (!aiFile.exists()) {
    //         throw new IOException("AI 파일이 저장되지 않았거나 존재하지 않습니다: " + aiFilePath);
    //     }
    
    //     // Define the output PDF path
    //     String outputPdfPath = fileSaveLocation + File.separator + 
    //             aiFile.getName().replace(".ai", ".pdf");

    
    //     // Attempt to load the AI file as a PDF using PDFBox
    //     try (PDDocument document = PDDocument.load(new File(outputPdfPath))) {
    //         // Save the loaded document as a PDF
    //         document.save(outputPdfPath.getAbsolutePath());
    //         System.out.println("AI 파일을 PDF로 성공적으로 변환했습니다: " + outputPdfPath);
    //     } catch (IOException e) {
    //         System.err.println("PDFBox로 AI 파일을 변환하는 데 실패했습니다: " + e.getMessage());
    //         throw new IOException("AI 파일이 PDF 형식으로 호환되지 않거나 손상되었습니다.", e);
    //     }
    
    //     return outputPdfPath;
    // }
    public static String copyFileWithNewExtension(String sourceFilePath, String targetDirectory, String newExtension) throws IOException {
        // 원본 파일 객체 생성
        File sourceFile = new File(sourceFilePath);
        
        // 파일이 존재하는지 확인
        if (!sourceFile.exists()) {
            throw new IOException("Source file does not exist: " + sourceFilePath);
        }

        // 새로운 파일 이름 생성 (확장자만 변경)
        String targetFileName = sourceFile.getName().replaceFirst("[.][^.]+$", "") + "." + newExtension;

        // 대상 경로 (디렉토리 + 새로운 파일 이름)
        Path targetFilePath = new File(targetDirectory, targetFileName).toPath();

        // 파일 복사
        Files.copy(sourceFile.toPath(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File copied with new extension: " + targetFilePath);

        return targetDirectory+targetFileName;
    }

    public static String changeFileExtension(String sourceFilePath, String newExtension) throws IOException {
        // 원본 파일 객체 생성
        File sourceFile = new File(sourceFilePath);

        // 파일 이름과 확장자 분리
        String fileNameWithoutExtension = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.'));

        // 새 파일 경로 생성 (새 확장자를 추가)
        String destinationFilePath = sourceFile.getParent() + File.separator + fileNameWithoutExtension + "." + newExtension;

        // 새 파일 객체 생성
        File destinationFile = new File(destinationFilePath);

        // 원본 파일을 새 파일로 복사
        if (sourceFile.renameTo(destinationFile)) {
            System.out.println("파일 확장자가 변경되었습니다: " + destinationFile.getAbsolutePath());
        } else {
            System.out.println("파일 확장자 변경에 실패했습니다.");
        }

        return destinationFilePath;
    }

    public String uploadFile(@RequestParam("file") MultipartFile file, String uploadFolder) {
        // 업로드된 파일 이름 가져오기
        String fileName = file.getOriginalFilename();

        

        String fileKind = getFileExtension(fileName);

        if(fileKind == "pdf"){
            uploadFolder = uploadFolder+"pdf";
        } else if (fileKind == "ai") {
            uploadFolder = uploadFolder + "ai";
        } else {
            uploadFolder = uploadFolder + "jpg";
        }
        try {
            // 파일 저장 경로 생성
            File saveFile = new File(uploadFolder+fileName);

            // 디렉토리가 존재하지 않으면 생성
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            // 파일 저장
            file.transferTo(saveFile);

            return "파일이 성공적으로 업로드되었습니다. 저장 위치: " + saveFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "파일 업로드 중 오류가 발생했습니다.";
        }
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "파일 이름이 비어 있습니다.";
        }

        // 파일 이름에 '.'이 포함되어 있는지 확인
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1); // 확장자 반환
        } else {
            return "확장자가 없습니다."; // 확장자가 없는 경우
        }
    }

    public void convertPdfToSvg(String pdfFilePath, String svgFilePath) throws IOException {
        PDDocument document = null; // Initialize to null
        try {
            document = PDDocument.load(new File(pdfFilePath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
    
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300);
    
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    
                TranscoderInput input = new TranscoderInput(bais);
    
                FileOutputStream fos = new FileOutputStream(new File(svgFilePath.replace(".svg", "_" + page + ".svg")));
                TranscoderOutput output = new TranscoderOutput(fos);
    
                Transcoder transcoder = new PNGTranscoder();
                try {
                    transcoder.transcode(input, output);
                } catch (TranscoderException e) {
                    throw new IOException("Error converting to SVG: " + e.getMessage(), e); // Wrap and rethrow
                } finally {
                    fos.close();
                    baos.close();
                    bais.close();
                }
            }
        } finally {  // Ensure document is closed even if exception occurs
            if (document != null) {
                document.close();
            }
        }
    }

    public void convertPdfToPng(String pdfFilePath, String pngFilePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300);
                File file = new File(pngFilePath);
                ImageIO.write(image, "png", file);
            }
        }
    }

    public static String convertPngToJpg(String pngFilePath, String jpgFilePath) throws IOException {
        File pngFile = new File(pngFilePath);
        if (!pngFile.exists()) {
            System.err.println("PNG file not found: " + pngFilePath);
            // Handle the error appropriately (e.g., throw an exception)
            throw new IOException("PNG file not found: " + pngFilePath);
        }
        BufferedImage image = ImageIO.read(pngFile);
    
        File jpgFile = new File(jpgFilePath);
        ImageIO.write(image, "jpg", jpgFile); // PNG 이미지를 JPG 형식으로 저장
    
        System.out.println("PNG 파일을 JPG 파일로 변환했습니다: " + jpgFile.getAbsolutePath());
        return jpgFilePath;
    }


    //OCR 인식
    public String ocrReturnString(String imageName){
         // 1. Tesseract 객체 생성
         Tesseract tesseract = new Tesseract();

         // 2. Tesseract OCR 데이터 디렉토리 설정 (필요한 경우)
         tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // 예시: tessdata 디렉토리 절대 경로

         // 3. Tesseract OCR - 한국어 설정
         tesseract.setLanguage("kor");
        //  tesseract.setPageSegMode(0);
        //  tesseract.setOcrEngineMode(OcrEngineMode.LSTM_ONLY);

         // 4. 이미지 파일 경로 설정
         String imagePath = uploadFolder+imageName; // 이미지 파일 경로

         File imageFile = new File(imagePath);

         if(!imageFile.exists()){
            System.err.println("이미지 파일이 존재하지 않습니다: " + imagePath);
            return null; // 또는 적절한 예외 처리
         }

        String result = "";
 
         try {
             // 4. OCR 수행
             result = tesseract.doOCR(new File(imagePath));

             result.replaceAll("\n","");
             result.replaceAll("|", "");

         } catch (TesseractException e) {
            System.err.println("OCR 수행 중 오류 발생: " + e.getMessage());
            // 예외 처리 (로그 출력, 예외 다시 던지기 등)
            return result; // 또는 빈 문자열 반환
         }
         return result;
    }





    // public String aiConvertSvg(String aiFilePath, String svgOutputPath){

    //     String result = "";

    //     try {
    //         // AI 파일 로드
    //         AiImage aiImage = (AiImage) Image.load(aiFilePath);

    //         // SVG 옵션 설정 및 변환
    //         SvgOptions options = new SvgOptions();
    //         aiImage.save(svgOutputPath, options);

    //         System.out.println("SVG 변환 성공: " + svgOutputPath);

    //         result = svgOutputPath;
            
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return svgOutputPath;
    // }

    

    

}
