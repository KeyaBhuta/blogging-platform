const BASE = 'http://localhost:8080/api';
function getToken() { return localStorage.getItem('token'); }
function getUsername() { return localStorage.getItem('username'); }
function getUserId() { return localStorage.getItem('userId'); }
function isLoggedIn() { return !!getToken(); }
function authHeaders() {
return {
'Content-Type': 'application/json',
'Authorization': 'Bearer ' + getToken()
};
}
// ■■ AUTH ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
async function register(username, email, password) {
const res = await fetch(`${BASE}/auth/register`, {
method: 'POST',
headers: { 'Content-Type': 'application/json' },
body: JSON.stringify({ username, email, password })
});
return res.json();
}
async function login(username, password) {
const res = await fetch(`${BASE}/auth/login`, {
method: 'POST',
headers: { 'Content-Type': 'application/json' },
body: JSON.stringify({ username, password })
});
const data = await res.json();
if (data.token) {
localStorage.setItem('token', data.token);
localStorage.setItem('username', data.username);
localStorage.setItem('userId', data.userId);
}
return data;
}
function logout() {
localStorage.clear();
window.location.href = 'login.html';
}
// ■■ BLOGS ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
async function getBlogs(search = '', categoryId = '') {
    const url = `${BASE}/blogs?search=${search}&categoryId=${categoryId}`;
    const res = await fetch(url);
    return res.json();
    }
    async function getBlog(id) {
    const res = await fetch(`${BASE}/blogs/${id}`);
    return res.json();
    }
    async function createBlog(title, content, categoryId) {
    const res = await fetch(`${BASE}/blogs`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ title, content, categoryId })
    });
    return res.json();
    }
    async function deleteBlog(id) {
    const res = await fetch(`${BASE}/blogs/${id}`, {
    method: 'DELETE', headers: authHeaders()
    });
    return res.json();
    }
    // ■■ LIKES ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    async function likeBlog(blogId) {
    const res = await fetch(`${BASE}/likes/${blogId}`, {
    method: 'POST', headers: authHeaders()
    });
    return res.json();
    }
    async function unlikeBlog(blogId) {
    const res = await fetch(`${BASE}/likes/${blogId}`, {
    method: 'DELETE', headers: authHeaders()
    });
    return res.json();
    }
    async function getLikeCount(blogId) {
        const res = await fetch(`${BASE}/likes/${blogId}/count`);
        return res.json();
    }
    // ■■ COMMENTS ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    async function getComments(blogId) {
    const res = await fetch(`${BASE}/comments/blog/${blogId}`);
    return res.json();
    }
    async function postComment(blogId, content) {
    const res = await fetch(`${BASE}/comments`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ blogId, content })
    });
    return res.json();
    }
    async function deleteComment(commentId) {
        const res = await fetch(`${BASE}/comments/${commentId}`, {
            method: 'DELETE', headers: authHeaders()
        });
        return res.json();
    }
    // ■■ FOLLOWS ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    async function followUser(userId) {
    const res = await fetch(`${BASE}/follows/${userId}`, {
    method: 'POST', headers: authHeaders()
    });
    return res.json();
    }
    async function unfollowUser(userId) {
    const res = await fetch(`${BASE}/follows/${userId}`, {
    method: 'DELETE', headers: authHeaders()
    });
    return res.json();
    }
    // ■■ BOOKMARKS ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    async function bookmarkBlog(blogId) {
    const res = await fetch(`${BASE}/bookmarks/${blogId}`, {
    method: 'POST', headers: authHeaders()
    });
    return res.json();
    }
    async function getMyBookmarks() {
    const res = await fetch(`${BASE}/bookmarks`, { headers: authHeaders() });
    return res.json();
    }
    // ■■ PROFILE ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    async function getProfile(userId) {
    const res = await fetch(`${BASE}/users/${userId}`, { headers: authHeaders() });
    return res.json();
    }
    async function updateProfile(bio, profilePic) {
    const res = await fetch(`${BASE}/users/profile`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify({ bio, profilePic })
    });
    return res.json();
    }