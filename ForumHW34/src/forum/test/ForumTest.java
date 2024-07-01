package forum.test;

import forum.dao.Forum;
import forum.dao.ForumImpl;
import forum.model.Post;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ForumTest {
    Forum forum;
    Post[] posts;
    Comparator<Post> comparator = (p1, p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());
        return res != 0 ? res : p1.getDate().compareTo(p2.getDate());
    };

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        forum = new ForumImpl();
        posts = new Post[6];
        posts[0] = new Post(1, "title1", "author1", "content1");
        posts[1] = new Post(2, "title2", "author2", "content2");
        posts[2] = new Post(3, "title3", "author3", "content3");
        posts[3] = new Post(4, "title4", "author3", "content4");
        posts[4] = new Post(5, "title5", "author1", "content5");
        posts[5] = new Post(6, "title6", "author2", "content6");
        for (int i = 0; i < posts.length; i++) {
            forum.addPost(posts[i]);
        }
    }

    @org.junit.jupiter.api.Test
    void addPost() {
        assertFalse(forum.addPost(null));
        assertFalse(forum.addPost(posts[3]));
        Post post = new Post(7, "title7", "author2", "conent7");
        assertTrue(forum.addPost(post));
        assertEquals(7, forum.size());
        post = new Post(7, "title7", "author2", "conent7");
        assertFalse(forum.addPost(post));
    }

    @org.junit.jupiter.api.Test
    void removePost() {
        assertFalse(forum.removePost(9));
        assertTrue(forum.removePost(4));
        assertEquals(5, forum.size());
        assertFalse(forum.removePost(4));
    }

    @org.junit.jupiter.api.Test
    void updatePost() {
        assertTrue(forum.updatePost(4, "content0"));
        assertEquals("content0", forum.getPostById(4).getContent());
    }

    @org.junit.jupiter.api.Test
    void getPostById() {
        assertEquals(posts[3], forum.getPostById(4));
        assertNull(forum.getPostById(10));
    }

    @org.junit.jupiter.api.Test
    void getPostsByAuthor() {
        Post[] actual = forum.getPostsByAuthor("author3");
        Arrays.sort(actual, comparator);
        Post[] expected = {posts[2], posts[3]};
        assertArrayEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void testGetPostsByAuthor() {
        Post[] actual = forum.getPostsByAuthor("author1", LocalDate.now(), LocalDate.now());
        Arrays.sort(actual, comparator);
        Post[] expected = {posts[0], posts[4]};
        assertArrayEquals(expected, actual);
        Post[] actual1 = forum.getPostsByAuthor("author1", LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        Arrays.sort(actual, comparator);
        Post[] expected1 = new Post[0];
        assertArrayEquals(expected1, actual1);
    }

    @org.junit.jupiter.api.Test
    void size() {
        assertEquals(6, forum.size());
    }
}