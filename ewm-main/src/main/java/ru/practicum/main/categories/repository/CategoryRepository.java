package ru.practicum.main.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.categories.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
