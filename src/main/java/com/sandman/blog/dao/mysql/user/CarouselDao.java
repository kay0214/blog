package com.sandman.blog.dao.mysql.user;

import com.sandman.blog.entity.user.Carousel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselDao {
    public List<Carousel> getAllCarousel();
}
