package Try_it.common.util;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileRemove {
    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final String RV_DIR = "img/goods/";
    private static final String RV_DIR1 = "img/reviews/";

    public void removeFile(String fileName) {
        String filePath = fileName.startsWith(RV_DIR)? RV_DIR : RV_DIR1;
        amazonS3Client.deleteObject(bucket, filePath + fileName);
    }
}
