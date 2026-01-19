package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment, User author) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                UserMapper.mapToUserShortDto(author),
                comment.getCreated(),
                comment.isEdited());
    }

    public Comment toEntity(NewCommentDto newCommentDto) {
        String newCommentDtoText = newCommentDto.text();
        Comment comment = new Comment();
        comment.setText(newCommentDtoText);
        return comment;
    }
}
