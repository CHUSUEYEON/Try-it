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
    final private FileRemove fileRemove;

    public ReviewController(ReviewService reviewService, FileUpload fileUpload, FileRemove fileRemove) {
        this.reviewService = reviewService;
        this.fileUpload = fileUpload;
        this.fileRemove = fileRemove;
    }
    @Operation(summary = "리뷰 등록", description = "requestbody : 리뷰내용, 별점, (사진) / path : 상품Pk/토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 등록 성공"),
        @ApiResponse(responseCode = "400", description = "리뷰 등록 실패")
    })
    @PostMapping("/{goodsPk}")
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

    @Operation(summary = "리뷰 수정", description = "requestbody : 리뷰내용, 별점 사진(필수 아님) / path : 리뷰Pk/해당 유저 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
        @ApiResponse(responseCode = "400", description = "리뷰 수정 실패")
    })
    @PatchMapping("/{reviewPk}")
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

    @Operation(summary = "리뷰 삭제", description = "path : 리뷰Pk / 해당 토큰 필요")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "리뷰 삭제 실패")
    })
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


//    @DeleteMapping("/img/{reviewPk}")
//    public ResponseEntity<ResDTO> deleteReviewImg(@PathVariable Long reviewPk,
//                                                  @AuthenticationPrincipal String userPk){
//        ReviewEntity deletedReviewImg = reviewService.deleteReviewImg(reviewPk, userPk);
//        return ResponseEntity.ok().body(ResDTO.builder()
//           .statusCode(StatusCode.OK)
//           .data(deletedReviewImg)
//           .message("리뷰 이미지 삭제 성공")
//           .build());
//    }




}
