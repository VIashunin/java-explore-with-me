package ru.practicum.main.users.individual.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.individual.service.IndividualEventService;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.model.User;
import ru.practicum.main.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.users.mapper.UserMapper.mapFromUserToUserDto;

@Service
@Transactional
@RequiredArgsConstructor
public class IndividualUserServiceImpl implements IndividualUserService {
    private final UserRepository userRepository;
    private final IndividualEventService individualEventService;

    @Override
    public UserDto subscribe(int userId, int subsId) {
        if (userId == subsId) {
            throw new ConflictException("You can't follow yourself.");
        }
        User user = findUser(userId);
        User subscription = findUser(subsId);
        if (!subscription.isSubscriptionPermission()) {
            throw new ConflictException("The user has denied following himself.");
        }
        if (!user.getSubscriptions().contains(subscription)) {
            subscription.getSubscribers().add(user);
            user.getSubscriptions().add(subscription);
            userRepository.save(subscription);
            return mapFromUserToUserDto(userRepository.save(user));
        } else {
            throw new ConflictException("Such subscription  is already exists.");
        }
    }

    @Override
    public UserDto unsubscribe(int userId, int subsId) {
        if (userId == subsId) {
            throw new ConflictException("You can't unsubscribe yourself.");
        }
        User user = findUser(userId);
        User subscription = findUser(subsId);
        boolean firstCondition = user.getSubscriptions() != null && !user.getSubscriptions().isEmpty();
        boolean secondCondition = subscription.getSubscribers() != null && !subscription.getSubscribers().isEmpty();
        if (!(firstCondition && secondCondition)) {
            throw new ConflictException("Data integrity violation has occurred.");
        }
        if (user.getSubscriptions().contains(subscription)) {
            user.getSubscriptions().remove(subscription);
            subscription.getSubscribers().remove(user);
            userRepository.save(subscription);
            return mapFromUserToUserDto(userRepository.save(user));
        } else {
            throw new ConflictException("Such subscription does not exist, so it cannot be canceled.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getSubscriptionEvents(int userId, int from, int size) {
        User user = findUser(userId);
        List<EventShortDto> subscriptionsEventsList = new ArrayList<>();
        if (!user.getSubscriptions().isEmpty()) {
            for (User subscription : user.getSubscriptions()) {
                subscriptionsEventsList.addAll(individualEventService.getEventsByUser(subscription.getId(), from, size));
            }
        } else {
            throw new ConflictException("There are no registered subscriptions.");
        }
        return subscriptionsEventsList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getSubscriptionByIdEvents(int userId, int subsId, int from, int size) {
        User user = findUser(userId);
        User subscription = findUser(subsId);
        if (!user.getSubscriptions().isEmpty()) {
            if (user.getSubscriptions().contains(subscription)) {
                return individualEventService.getEventsByUser(subsId, from, size);
            } else {
                throw new ConflictException("Such subscription does not exist.");
            }
        } else {
            throw new ConflictException("There are no registered subscriptions.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getSubscriberEvents(int userId, int from, int size) {
        User user = findUser(userId);
        List<EventShortDto> subscribersEventsList = new ArrayList<>();
        if (!user.getSubscribers().isEmpty()) {
            for (User subscriber : user.getSubscribers()) {
                subscribersEventsList.addAll(individualEventService.getEventsByUser(subscriber.getId(), from, size));
            }
        } else {
            throw new ConflictException("There are no registered subscribers.");
        }
        return subscribersEventsList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getSubscriberByIdEvents(int userId, int subsId, int from, int size) {
        User user = findUser(userId);
        User subscriber = findUser(subsId);
        if (!user.getSubscribers().isEmpty()) {
            if (user.getSubscribers().contains(subscriber)) {
                return individualEventService.getEventsByUser(subsId, from, size);
            } else {
                throw new ConflictException("Such subscriber does not exist.");
            }
        } else {
            throw new ConflictException("There are no registered subscribers.");
        }
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found."));
    }
}