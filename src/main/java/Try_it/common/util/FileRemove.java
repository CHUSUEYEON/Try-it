package Try_it.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FileRemove {

    @Autowired
    private final FileUpload fileUpload;

    public FileRemove(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final String RV_DIR = "img/goods/";
    private static final String RV_DIR1 = "img/reviews/";



    public void removeGoodsFile(Long goodsPk) {
        final String files = String.format("goods_%d", goodsPk);
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, RV_DIR);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()){
            String filename = os.getKey();
            log.info("filename: {}", filename);
            if(filename.startsWith(RV_DIR+files)){
                amazonS3Client.deleteObject(bucket, filename);
        }
        }
//        String filePath = fileName.startsWith(RV_DIR)? RV_DIR : RV_DIR1;
//
//        String bucketName = "your-bucket-name";
//        String prefix = "your-folder/"; // 특정 폴더의 파일만 불러오려면 이 부분에 경로를 추가
//
//        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
//
//        // S3 버킷의 모든 오브젝트(파일) 목록 가져오기
//        ObjectListing objectListing = s3Client.listObjects(bucketName, prefix);
//
//        // 오브젝트 목록에서 파일 이름(오브젝트 키) 출력
//        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
//            System.out.println("File Name: " + os.getKey());
//        }

    }

public void removeReviewsFile(Long reviewPk) {
    final String files = String.format("reviews_%d", reviewPk);
    ObjectListing objectListing = amazonS3Client.listObjects(bucket, RV_DIR1);
    for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
        String filename = os.getKey();
        log.info("filename: {}", filename);
        if (filename.startsWith(RV_DIR1 + files)) {
            amazonS3Client.deleteObject(bucket, filename);
        }
    }
    }
}


//파일이름 만드는 메소드 이용해서 삭제
//1. 다 삭제하고 업로드(사용자가 너무 불편함) - 일단 이렇게 가기!!