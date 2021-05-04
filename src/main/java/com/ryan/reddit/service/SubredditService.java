package com.ryan.reddit.service;

import com.ryan.reddit.dto.SubredditDto;
import com.ryan.reddit.exception.SpringRedditException;
import com.ryan.reddit.mapper.SubredditMapper;
import com.ryan.reddit.model.Subreddit;
import com.ryan.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;

    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Optional<Subreddit> optionalSubreddit =  subredditRepository.findByName(subredditDto.getName());
        if(optionalSubreddit.isPresent()){
            throw new SpringRedditException("This Subreddit Existed !");
        }
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
