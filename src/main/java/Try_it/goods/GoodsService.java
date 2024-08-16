package Try_it.goods;

import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Slf4j
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository, UserRepository userRepository) {
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloudfront-domain-name}")
    private String cloudfront;
    private static final String RV_DIR = "img/goods/";

    public GoodsEntity createGoods(final GoodsDTO goodsDTO,
                                   final MultipartFile file,
                                   final String userIdx
                                   ) throws Exception{
        // 토큰 확왼
        UserEntity user = userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        // 파일 경로
        UUID uuid = UUID.randomUUID();
        String fileName = RV_DIR + uuid + "_" + file.getOriginalFilename();
        String fileUrl = "https://" + cloudfront + "/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        GoodsEntity newGoods = GoodsEntity.builder()
            .goodsName(goodsDTO.getGoodsName())
            .goodsDescription(goodsDTO.getGoodsDescription())
            .goodsPrice(goodsDTO.getGoodsPrice())
            .goodsFile(fileUrl)
            .goodsCreatedAt(goodsDTO.getGoodsCreatedAt())
            .build();
        return goodsRepository.save(newGoods);
    }
}
