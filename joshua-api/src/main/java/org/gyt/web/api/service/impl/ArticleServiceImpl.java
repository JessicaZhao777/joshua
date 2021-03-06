package org.gyt.web.api.service.impl;

import org.gyt.web.api.repository.ArticleRepository;
import org.gyt.web.api.service.ArticleService;
import org.gyt.web.api.service.FellowshipService;
import org.gyt.web.api.service.UserService;
import org.gyt.web.model.Article;
import org.gyt.web.model.ArticleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FellowshipService fellowshipService;

    @Override
    public Article get(Long id) {
        return articleRepository.findOne(id);
    }

    @Override
    public List<Article> getFromFellowship(String name) {
        return articleRepository.findAll().stream().filter(article -> article.getFellowship().getName().equals(name)).collect(Collectors.toList());
    }

    @Override
    public List<Article> getFromUser(String username) {
        return articleRepository.findAll().stream().filter(article -> article.getAuthor().getUsername().equals(username) || article.getAuditor().getUsername().equals(username)).collect(Collectors.toList());
    }

    @Override
    public boolean create(Article article) {
        return articleRepository.save(article) != null;
    }

    @Override
    public boolean enable(Long id) {
        Article article = articleRepository.findOne(id);

        if (null != article && article.isDisable()) {
            article.setDisable(false);
            articleRepository.save(article);
        }

        return false;
    }

    @Override
    public boolean isEnabled(Long id) {
        Article article = articleRepository.findOne(id);
        return null != article && article.isDisable();
    }

    @Override
    public boolean disable(Long id) {
        Article article = articleRepository.findOne(id);

        if (null != article && !article.isDisable()) {
            article.setDisable(true);
            articleRepository.save(article);
            return true;
        }

        return false;
    }

    @Override
    public boolean audit(Long id) {
        Article article = articleRepository.findOne(id);

        if (null != article && !article.isDisable()) {
            article.setStatus(ArticleStatus.AUDITING);
            articleRepository.save(article);
            return true;
        }

        return false;
    }

    @Override
    public boolean publish(Long id) {
        Article article = articleRepository.findOne(id);

        if (null != article && !article.isDisable()) {
            article.setStatus(ArticleStatus.PUBLISHED);
            articleRepository.save(article);
            return true;
        }

        return false;
    }
}
