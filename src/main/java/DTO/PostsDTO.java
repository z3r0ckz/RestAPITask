package DTO;

import java.util.Objects;

public class PostsDTO {
    private int userId;
    private int id;
    private String title;
    private String body;

    // Default constructor
    public PostsDTO() {
    }

    public PostsDTO(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    // Getters and setters

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    // Equals, hashCode, and toString methods

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PostsDTO other = (PostsDTO) obj;
        return id == other.id &&
                userId == other.userId &&
                Objects.equals(title, other.title) &&
                Objects.equals(body, other.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, body);
    }

    @Override
    public String toString() {
        return "PostsDTO{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
