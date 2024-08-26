package Try_it.admin;

import Try_it.category.CategoriesEntity;
import Try_it.category.CategoryDTO;
import Try_it.category.CategoryRepository;
import Try_it.common.util.FileUpload;
import Try_it.goods.GoodsDTO;
import Try_it.goods.entity.GoodsCategoriesMappingEntity;
import Try_it.goods.entity.GoodsEntity;
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
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdminGoodsService {
    private final AdminGoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FileUpload fileUpload;

    @Autowired
    public AdminGoodsService(AdminGoodsRepository goodsRepository, UserRepository userRepository, FileUpload fileUpload, CategoryRepository categoryRepository) {
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
        this.fileUpload = fileUpload;
        this.categoryRepository = categoryRepository;
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
                                   MultipartFile file,
                                   final Long goodsPk,
                                   final String userPk
                                   ) throws Exception{

        userRepository.findAdminByUserPk(Long.valueOf(userPk))
            .orElseThrow(() -> new RuntimeException("관리자로 로그인을 해주세요."));
        GoodsEntity goods = goodsRepository.findById(goodsPk).orElseThrow(()-> new RuntimeException("해당되는 상품이 없습니다."));

        if(file != null && !file.isEmpty()){
        String fileUrl = createFilename(file);
        GoodsEntity updatedGoodsWithPhoto = GoodsEntity.builder()
            .goodsName(goodsDTO.getGoodsName())
            .goodsPrice(goodsDTO.getGoodsPrice())
            .goodsImgCount(goodsDTO.getGoodsImgCount())
            .goodsPk(goods.getGoodsPk())
            .goodsCreatedAt(goods.getGoodsCreatedAt())
            .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
            .goodsDescription(goodsDTO.getGoodsDescription())
            .build();
        return goodsRepository.save(updatedGoodsWithPhoto);
        }else{
            GoodsEntity updatedGoods = GoodsEntity.builder()
               .goodsName(goodsDTO.getGoodsName())
               .goodsPrice(goodsDTO.getGoodsPrice())
               .goodsPk(goods.getGoodsPk())
                .goodsImgCount(goods.getGoodsImgCount())
               .goodsCreatedAt(goods.getGoodsCreatedAt())
               .goodsUpdatedAt(goodsDTO.getGoodsUpdatedAt())
               .goodsDescription(goodsDTO.getGoodsDescription())
               .build();
            return goodsRepository.save(updatedGoods);
        }
    }

    public String createFilename(MultipartFile file) throws Exception{
        //Todo : db에 g_file이 아니라 g_img_cnt (상품 사진 개수로 int 저장)하고, 경로는 저장하지 않는다.
        //Todo : 파일 이름 ex) imggggg_0_1 : 0번(pk)를 가진 굿즈의 두 번째 imggggg
        UUID uuid = UUID.randomUUID();
        String fileName = RV_DIR + uuid + "_" + file.getOriginalFilename();
        String fileUrl = "https://" + cloudfront + "/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        return fileUrl;
    }

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
