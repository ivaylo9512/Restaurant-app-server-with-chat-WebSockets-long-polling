package com.vision.project.services;

import com.vision.project.models.*;
import com.vision.project.repositories.base.*;
import com.vision.project.services.base.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;


    public OrderServiceImpl(OrderRepository orderRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Order create(Order order, int restaurantId, int userId){
        if(order.getDishes() == null || order.getDishes().size() == 0){
            throw new IllegalArgumentException("Order must have at least one dish");
        }

        for (Dish dish : order.getDishes()) {
            dish.setOrder(order);
        }

        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        order.setRestaurant(restaurant);

        order.setUser(userRepository.getOne(userId));
        order = orderRepository.save(order);

        return order;
    }

    @Transactional
    @Override
    public Dish update(int orderId, int dishId, int userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order doesn't exist."));

        List<Dish> notReady = new ArrayList<>();
        boolean updated = false;

        Dish updatedDish = null;
        for (Dish orderDish: order.getDishes()) {
            if(orderDish.getId() == dishId ){
                if(!orderDish.getReady()) {
                    orderDish.setUpdatedBy(userRepository.getOne(userId));
                    orderDish.setReady(true);
                    order.setUpdated(LocalDateTime.now());

                    updated = true;
                }
                updatedDish = orderDish;
            }
            if(!orderDish.getReady()){
                notReady.add(orderDish);
            }
        }
        if(notReady.size() == 0){
            order.setReady(true);
        }

        if(updated) {
            order = orderRepository.save(order);
        }

        updatedDish.setUpdated(order.getUpdated());

        return updatedDish;
    }


    @Override
    public Order findById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order doesn't exist."));
    }

    @Override
    public Map<Integer, Order> findNotReady(int restaurantId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "created");

        return orderRepository.findNotReady(restaurantRepository.getOne(restaurantId), pageable).stream()
                .collect(Collectors.toMap(Order::getId, order -> order, (existing, replacement) -> existing, LinkedHashMap::new));
    }

    @Override
    public List<Order> findAllNotReady(int restaurantId) {
        return orderRepository.findAllNotReady(restaurantRepository.getOne(restaurantId));
    }

    @Override
    public List<Order> findMoreRecent(LocalDateTime lastCheck, int restaurantId) {
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        return orderRepository.findMoreRecent(lastCheck, restaurant);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

}
