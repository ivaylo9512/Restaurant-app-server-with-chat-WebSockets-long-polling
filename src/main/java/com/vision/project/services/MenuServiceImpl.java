package com.vision.project.services;

import com.vision.project.models.Menu;
import com.vision.project.repositories.base.MenuRepository;
import com.vision.project.services.base.MenuService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    private MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public void delete(int id) {
       menuRepository.deleteById(id);
    }

    @Override
    public Menu create(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public List<Menu> findByRestaurant(int restaurant) {
        return menuRepository.findByRestaurant(restaurant);
    }
}
