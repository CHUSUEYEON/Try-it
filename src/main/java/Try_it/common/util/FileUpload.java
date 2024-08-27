package Try_it.common.util;

import Try_it.goods.GoodsDTO;
import Try_it.goods.GoodsEntity;
import Try_it.review.ReviewDTO;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUpload {
    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final String RV_DIR = "img/goods/";
    private static final String RV_DIR1 = "img/reviews/";

    public List<String> generateFileName(GoodsDTO goodsDTO, List<MultipartFile> files){
        List<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < files.size() ; i ++ ){
            MultipartFile file = files.get(i);
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ""; // 기본값을 빈 문자열로 설정

            String formattedFileName = String.format(
                "%s_%d_%d%s", goodsDTO.getGoodsName(), goodsDTO.getGoodsPk(), i, extension
                );
            fileNames.add(formattedFileName);
        }
        return fileNames;
    }

    public void uploadFile(List<MultipartFile> files, List<String> fileNames) throws IOException {
        for(int i = 0; i< files.size(); i++){
            MultipartFile file = files.get(i);
            String fileName = RV_DIR + fileNames.get(i);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        }
    }

    public List<String> generateReviewFileName(ReviewDTO reviewDTO, GoodsEntity goods, List<MultipartFile> files){
        List<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < files.size() ; i ++ ){
            MultipartFile file = files.get(i);
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ""; // 기본값을 빈 문자열로 설정

            String formattedFileName = String.format(
                "%s_%d_%d%s", goods.getGoodsName(), reviewDTO.getReviewPk(), i, extension
            );
            fileNames.add(formattedFileName);
        }
        return fileNames;
    }
    // 상품 이름_리뷰 PK_파일 인덱스

    public void uploadReviewFile(List<MultipartFile> files, List<String> fileNames) throws IOException {
        for(int i = 0; i< files.size(); i++){
            MultipartFile file = files.get(i);
            String fileName = RV_DIR1 + fileNames.get(i);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        }
    }
}