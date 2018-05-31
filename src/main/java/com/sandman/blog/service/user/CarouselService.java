package com.sandman.blog.service.user;

import com.sandman.blog.dao.mysql.user.CarouselDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Carousel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselService {
    @Autowired
    private CarouselDao carouselDao;
    public BaseDto getAllCarousel(){
        List<Carousel> carouselList = carouselDao.getAllCarousel();
        return new BaseDto(ResponseStatus.SUCCESS,carouselList);
    }
}
