package Try_it.review;

import Try_it.common.dto.ResDTO;
import Try_it.common.util.FileRemove;
import Try_it.common.util.FileUpload;
import Try_it.common.vo.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/reviews")
@Tag(name = "Review", description = "리뷰 관련 API")
public class ReviewController {
    final private ReviewService reviewService;
    final private FileUpload fileUpload;

    public ReviewController(ReviewService reviewService, FileUpload fileUpload) {
        this.reviewService = reviewService;
        this.fileUpload = fileUpload;
    }
    @Operation(summary = "리뷰 등록", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            goodsPk : 4\s
            인증/인가 : 로그인 필요\s
            파일 첨부 시 [Add String item] 클릭하여 가능(선택)\s
            \s
            reviewDTO : { "reviewContent": "리뷰입니다", "reviewRate": 4 }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PostMapping(value = "/{goodsPk}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResDTO> createReview(@AuthenticationPrincipal String userPk,
                                               @RequestPart(required = false) List<MultipartFile> files,
                                               @Valid @RequestPart ReviewDTO reviewDTO,
                                               @PathVariable Long goodsPk
                                               ) throws Exception {
        ReviewEntity createdReview = reviewService.createReview(goodsPk, userPk, reviewDTO, files);
        ReviewDTO responseReviewDTO = reviewDTO.builder()
            .reviewPk(createdReview.getReviewPk())
            .reviewContent(createdReview.getReviewContent())
            .reviewRate(createdReview.getReviewRate())
            .reviewFile(createdReview.getReviewFile())
            .reviewCreatedAt(createdReview.getReviewCreatedAt())
            .reviewUpdatedAt(createdReview.getReviewUpdatedAt())
            .reviewDeletedAt(createdReview.getReviewDeletedAt())
            .build();

        List<String> fileNames = fileUpload.generateReviewFileName(responseReviewDTO.getReviewPk(), createdReview.getGoods(), files);
        fileUpload.uploadReviewFile(files, fileNames);


        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(responseReviewDTO)
           .message("리뷰 생성 성공")
           .build());
    }

    @Operation(summary = "리뷰 수정", description = """
            테스트 방법: 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            reviewPk : 1\s
            인증/인가 : 로그인 필요\s
            파일 첨부 시 [Add String item] 클릭하여 가능(선택)\s
            \s
            reviewDTO : { "reviewContent": "리뷰 수정 입니다.", "reviewRate": 5 }\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """
    )
    @PatchMapping(value = "/{reviewPk}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResDTO> updateReview(@AuthenticationPrincipal String userPk,
                                               @RequestPart(required = false) List<MultipartFile> files,
                                               @Valid @RequestPart ReviewDTO reviewDTO,
                                               @PathVariable Long reviewPk
                                               ) throws Exception {
        ReviewEntity updatedReview = reviewService.updateReview(reviewPk, userPk, reviewDTO, files);
        ReviewDTO responseReviewDTO = reviewDTO.builder()
            .reviewPk(updatedReview.getReviewPk())
            .reviewContent(updatedReview.getReviewContent())
            .reviewRate(updatedReview.getReviewRate())
            .reviewFile(updatedReview.getReviewFile())
            .reviewCreatedAt(updatedReview.getReviewCreatedAt())
            .reviewUpdatedAt(updatedReview.getReviewUpdatedAt())
            .reviewDeletedAt(updatedReview.getReviewDeletedAt())
            .build();

        return ResponseEntity.ok().body(ResDTO.builder()
            .statusCode(StatusCode.OK)
            .data(responseReviewDTO)
            .message("리뷰 수정 성공")
            .build());
    }

    @Operation(summary = "리뷰 삭제", description = """
            테스트 방법 : 아래 데이터를 복사하여 Request body 에 붙여넣고 사용\s
            reviewPk : 1\s
            인증/인가 : 로그인 필요\s
            \s
            *로그인 방법은 문서 최상단 설명 참고
        """)
    @DeleteMapping("/{reviewPk}")
    public ResponseEntity<ResDTO> deleteReview(@PathVariable Long reviewPk,
                                               @AuthenticationPrincipal String userPk){
        ReviewEntity deletedReview = reviewService.delete(reviewPk, userPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
            .data(deletedReview)
           .message("리뷰 삭제 성공")
           .build());
    }
}
