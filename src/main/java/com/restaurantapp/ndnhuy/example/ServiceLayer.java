package com.restaurantapp.ndnhuy.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ServiceLayer {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public long countAllDocsTransactional() {
        return countAllDocs();
    }

    public long countAllDocsNonTransactional() {
        return countAllDocs();
    }

    public long countDocumentsOfUser(long id) {
        return userRepository.findById(101L).get().getDocs().size();
    }

    private long countAllDocs() {
        return userRepository.findAll()
                .stream()
                .map(User::getDocs)
                .mapToLong(Collection::size)
                .sum();
    }

}
