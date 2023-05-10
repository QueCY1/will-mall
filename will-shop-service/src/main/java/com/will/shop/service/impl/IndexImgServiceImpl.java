package com.will.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.model.IndexImg;
import com.will.shop.dao.IndexImgMapper;
import com.will.shop.service.IndexImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService {

    private final IndexImgMapper indexImgMapper;

	@Override
	public void deleteIndexImgByIds(Long[] ids) {
		indexImgMapper.deleteIndexImgByIds(ids);
	}

    @Override
    @Cacheable(cacheNames = "indexImg", key = "'indexImg'")
    public List<IndexImg> listIndexImg() {
        return indexImgMapper.listIndexImg();
    }

    @Override
    @CacheEvict(cacheNames = "indexImg", key = "'indexImg'")
    public void removeIndexImgCache() {
    }
}
