package Try_it.admin;

import Try_it.category.*;
import Try_it.common.util.FileUpload;
import Try_it.goods.GoodsDTO;
import Try_it.goods.GoodsEntity;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdminGoodsService {
    private final AdminGoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GoodsCategoriesMappingRepository goodsCategoriesMappingRepository;
    private final FileUpload fileUpload;

    @Autowired
    public AdminGoodsService(AdminGoodsRepository goodsRepository, UserRepository userRepository, FileUpload fileUpload, CategoryRepository categoryRepository, GoodsCategoriesMappingRepository goodsCategoriesMappingRepository) {
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
        this.fileUpload = fileUpload;
        this.categoryRepository = categoryRepository;
        this.goodsCategoriesMappingRepository = goodsCategoriesMappingRepository;
    }

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloudfront-domain-name}")
    private String cloudfront;
    private static final String RV_DIR = "img/goods/";

    public GoodsEntity createGoods(final GoodsDTO goodsDTO,
                                   final CategoryDTO categoryDTO,
                                   final List<MultipartFile> files,
                                   final String userPk
                                   ) throws Exception{
        // 토큰 확왼
        userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));

        CategoriesEntity category = categoryRepository.findByCategoryName(categoryDTO.getCategoryName())
            .orElseThrow(() -> new RuntimeException("해당되는 카테고리가 없습니다."));

        GoodsEntity newGoods = GoodsEntity.builder()
            .goodsName(goodsDTO.getGoodsName())
            .goodsDescription(goodsDTO.getGoodsDescription())
            .goodsPrice(goodsDTO.getGoodsPrice())
            .goodsImgCount(files.size())
            .goodsCreatedAt(goodsDTO.getGoodsCreatedAt())
            .category(new ArrayList<>())
            .build();

        GoodsCategoriesMappingEntity mapping = GoodsCategoriesMappingEntity.builder()
            .goods(newGoods)
            .category(category)
            .build();
        newGoods.getCategory().add(mapping);

        return goodsRepository.save(newGoods);
    }

    public GoodsEntity updateGoods(final GoodsDTO goodsDTO,
                                   List<CategoryDTO> categoryDTOs,
                                   List<MultipartFile> files,
                                   final Long goodsPk,
                                   final String userPk
                                   ) throws Exception{

        userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));
        GoodsEntity goods = goodsRepository.findById(goodsPk).orElseThrow(()-> new RuntimeException("해당되는 상품이 없습니다."));

// 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            List<String> fileNames = fileUpload.generateFileName(goodsDTO, files);
            fileUpload.uploadFile(files, fileNames);
        }

        // 카테고리 업데이트
        List<GoodsCategoriesMappingEntity> newCategories = new ArrayList<>();
        if (categoryDTOs != null) {
            // 기존 카테고리 매핑 삭제
            List<GoodsCategoriesMappingEntity> existMappings = goodsCategoriesMappingRepository.findByGoods(goods);
            goodsCategoriesMappingRepository.deleteAll(existMappings);

            // 새 카테고리 추가
            for (CategoryDTO categoryDTO : categoryDTOs) {
                CategoriesEntity newCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName())
                    .orElseThrow(() -> new RuntimeException("해당되는 카테고리가 없습니다."));

                GoodsCategoriesMappingEntity newMapping = GoodsCategoriesMappingEntity.builder()
                    .goods(goods)
                    .category(newCategory)
                    .build();

                goodsCategoriesMappingRepository.save(newMapping);
                newCategories.add(newMapping);
            }
        }

        // 상품 업데이트
        GoodsEntity updatedGoods = GoodsEntity.builder()
            .goodsName(goodsDTO.getGoodsName())
            .goodsPrice(goodsDTO.getGoodsPrice())
            .goodsImgCount((files != null && !files.isEmpty()) ? files.size() : (goods.getGoodsImgCount() != null ? goods.getGoodsImgCount() : 0))
            .goodsPk(goods.getGoodsPk())
            .goodsCreatedAt(goods.getGoodsCreatedAt())
            .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
            .goodsDescription(goodsDTO.getGoodsDescription())
            .category(newCategories.isEmpty() ? goods.getCategory() : newCategories)
            .build();

        return goodsRepository.save(updatedGoods);
    }


//Todo : 상품 수정(카테고리 추가한 부분) 트러블 슈팅 작성하기

//        if(files != null && !files.isEmpty()){
//            List<String> fileNames = fileUpload.generateFileName(goodsDTO, files);
//            fileUpload.uploadFile(files, fileNames);
//            if(categoryDTOs == null){
//                GoodsEntity updatedGoodsWithPhoto = GoodsEntity.builder()
//                    .goodsName(goodsDTO.getGoodsName())
//                    .goodsPrice(goodsDTO.getGoodsPrice())
//                    .goodsImgCount(goodsDTO.getGoodsImgCount())
//                    .goodsPk(goods.getGoodsPk())
//                    .goodsCreatedAt(goods.getGoodsCreatedAt())
//                    .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
//                    .goodsDescription(goodsDTO.getGoodsDescription())
//                    .category(goods.getCategory())
//                    .build();
//                return goodsRepository.save(updatedGoodsWithPhoto);
//            }else{
//                for(CategoryDTO categoryDTO : categoryDTOs) {
//                    CategoriesEntity newCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName())
//                        .orElseThrow(() -> new RuntimeException("해당되는 카테고리가 없습니다."));
//
//
//                GoodsCategoriesMappingEntity newMapping = GoodsCategoriesMappingEntity.builder()
//                    .goods(goods)
//                    .category(newCategory)
//                    .build();
//
//                goodsCategoriesMappingRepository.save(newMapping);
//
//                GoodsEntity updatedGoodsWithPhoto = GoodsEntity.builder()
//                    .goodsName(goodsDTO.getGoodsName())
//                    .goodsPrice(goodsDTO.getGoodsPrice())
//                    .goodsImgCount(goodsDTO.getGoodsImgCount())
//                    .goodsPk(goods.getGoodsPk())
//                    .goodsCreatedAt(goods.getGoodsCreatedAt())
//                    .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
//                    .goodsDescription(goodsDTO.getGoodsDescription())
//                    .category(new ArrayList<>())
//                    .build();
//                updatedGoodsWithPhoto.getCategory().add(newMapping);
//
//                return goodsRepository.save(updatedGoodsWithPhoto);
//            }}
//        }else{
//            if(categoryDTOs == null){
//                GoodsEntity updatedGoods = GoodsEntity.builder()
//                   .goodsName(goodsDTO.getGoodsName())
//                   .goodsPrice(goodsDTO.getGoodsPrice())
//                   .goodsPk(goods.getGoodsPk())
//                    .goodsImgCount(goods.getGoodsImgCount())
//                   .goodsCreatedAt(goods.getGoodsCreatedAt())
//                   .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
//                   .goodsDescription(goodsDTO.getGoodsDescription())
//                    .category(goods.getCategory())
//                   .build();
//                return goodsRepository.save(updatedGoods);
//            }else{
//                for(CategoryDTO categoryDTO : categoryDTOs) {
//                    CategoriesEntity newCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName())
//                        .orElseThrow(() -> new RuntimeException("해당되는 카테고리가 없습니다."));
//
//
//                    GoodsCategoriesMappingEntity newMapping = GoodsCategoriesMappingEntity.builder()
//                        .goods(goods)
//                        .category(newCategory)
//                        .build();
//
//                    goodsCategoriesMappingRepository.save(newMapping);
//
//                GoodsEntity updatedGoods = GoodsEntity.builder()
//                    .goodsName(goodsDTO.getGoodsName())
//                    .goodsPrice(goodsDTO.getGoodsPrice())
//                    .goodsPk(goods.getGoodsPk())
//                    .goodsImgCount(goods.getGoodsImgCount())
//                    .goodsCreatedAt(goods.getGoodsCreatedAt())
//                    .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
//                    .goodsDescription(goodsDTO.getGoodsDescription())
//                    .category(new ArrayList<>())
//                    .build();
//
//                    updatedGoods.getCategory().add(newMapping);
//                    return goodsRepository.save(updatedGoods);
//                }
//            }
//        }
//        return null;

    public GoodsEntity delete(final Long goodsPk, final String userPk){
        GoodsEntity goods = goodsRepository.findById(goodsPk)
            .orElseThrow(()-> new RuntimeException("해당되는 상품이 없습니다."));
        UserEntity user = userRepository.findAdminByUserPk(Long.valueOf(userPk))
           .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));

        if (user != null && goods != null) {
            goodsRepository.delete(goods);
            return goods;
        }else throw new RuntimeException("상품 삭제 실패");
    }

    public Page<GoodsEntity> getGoodsList(final int page, final String sort, final String direction, final String userPk, final String keyword){
        userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortDirection, sort));

        if(keyword == null || keyword.isEmpty()){
            Page<GoodsEntity> goods = goodsRepository.findAll(pageable);
            if(goods == null){
                throw new IllegalStateException("등록된 상품이 없습니다.");
            }
            return goods;
        } else{
            Page<GoodsEntity> goods = goodsRepository.findAllByKeyword(keyword, pageable);
            if(goods == null){
                throw new IllegalStateException("등록된 상품이 없습니다.");
            }
            return goods;
        }

    }
//    public Page<GoodsDTO> getGoodsList(final int page, final String sort, final String direction, final String userPk){
//        userRepository.findAdminByUserPk(Long.valueOf(userPk))
//            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));
//
//        Pageable pageable = PageRequest.of(page -1, Pagination.PAGE_SIZE);
//
//        Page<GoodsDTO> goods = goodsRepository.getGoodsList(pageable, sort, direction);
//        if(goods == null){
//            throw new IllegalStateException("등록된 상품이 없습니다.");
//        }
//        return goods;
//    }

//    public List<GoodsEntity> getGoods(final String keyword, final String userPk) {
//        userRepository.findAdminByUserPk(Long.valueOf(userPk))
//            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));
//
//        List<GoodsEntity> goods = goodsRepository.findGoodsByKeyword(keyword);
//        if(goods == null){
//            throw new IllegalStateException("해당되는 상품이 없습니다.");
//        }
//        return goods;
//    }

}
