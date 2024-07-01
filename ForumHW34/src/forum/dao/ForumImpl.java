package forum.dao;

import forum.model.Post;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class ForumImpl implements Forum {
    private Post[] posts;
    private int size;
    private Comparator<Post> comparator = (p1, p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());
        return res != 0 ? res : p1.getDate().compareTo(p2.getDate());
    };

    public ForumImpl() {
        posts = new Post[0];
    }

    @Override
    public boolean addPost(Post post) {
        if (post == null || getPostById(post.getPostId()) != null) {
            return false;
        }
        Post[] postsCopy = Arrays.copyOf(posts, posts.length + 1);
        int index = Arrays.binarySearch(posts, 0, size, post, comparator);
        index = index >= 0 ? index : -index - 1;
        System.arraycopy(posts, index, postsCopy, index + 1, size - index);
        postsCopy[index] = post;
        posts = postsCopy;
        size++;
        return true;
    }

    @Override
    public boolean removePost(int postId) {
        for (int i = 0; i < size; i++) {
            if (posts[i].getPostId() == postId) {
                Post[] postsCopy = Arrays.copyOf(posts, posts.length - 1);
                System.arraycopy(posts, i + 1, postsCopy, i, size - i - 1);
                posts = postsCopy;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updatePost(int postId, String content) {
        Post post = getPostById(postId);
        if (post == null) {
            return false;
        }
        post.setContent(content);
        return true;
    }

    @Override
    public Post getPostById(int postId) {
        for (int i = 0; i < size; i++) {
            if (posts[i].getPostId() == postId) {
                return posts[i];
            }
        }
        return null;
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
        return findPostsByPredicate(p -> p.getAuthor().equals(author));
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
        Post pattern = new Post(Integer.MIN_VALUE, null, author, null);
        pattern.setDate(dateFrom.atStartOfDay());
        int from = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        from = from >= 0 ? from : -from - 1;
        pattern = new Post(Integer.MAX_VALUE, null, author, null);
        pattern.setDate(dateTo.atTime(LocalTime.MAX));
        int to = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        to = to >= 0 ? to : -to - 1;
        return Arrays.copyOfRange(posts, from, to);
    }

    @Override
    public int size() {
        return size;
    }

    private Post[] findPostsByPredicate(Predicate<Post> predicate) {
        Post[] res = new Post[size];
        int j = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(posts[i])) {
                res[j++] = posts[i];
            }
        }
        return Arrays.copyOf(res, j);
    }
}
