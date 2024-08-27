package Try_it.review;

import Try_it.admin.AdminGoodsRepository;
import Try_it.common.util.FileUpload;
import Try_it.goods.GoodsDTO;
import Try_it.goods.GoodsEntity;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class ReviewService {
    final private ReviewRepository reviewRepository;
    final private UserRepository userRepository;
    final private AdminGoodsRepository goodsRepository;
    final private FileUpload fileUpload;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, AdminGoodsRepository goodsRepository, FileUpload fileUpload) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
        this.fileUpload = fileUpload;
    }

    public ReviewEntity createReview(final Long goodsPk,
                                     final String userPk,
                                     final ReviewDTO reviewDTO,
                                     final List<MultipartFile> files
                                    ) throws Exception {
        UserEntity user = userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));

        GoodsEntity goods = goodsRepository.findById(goodsPk).orElseThrow(()
            -> new RuntimeException("해당되는 상품이 없습니다."));

        ReviewEntity newReview = ReviewEntity.builder()
            .goods(goods)
            .user(user)
            .reviewFile(files.size())
            .reviewRate(reviewDTO.getReviewRate())
            .reviewContent(reviewDTO.getReviewContent())
            .reviewCreatedAt(reviewDTO.getReviewCreatedAt())
            .build();

        ReviewEntity savedReview = reviewRepository.save(newReview);
        List<String> fileNames = fileUpload.generateReviewFileName(reviewDTO, goods, files);
        fileUpload.uploadReviewFile(files, fileNames);

        return savedReview;

    }
}
