const BASE = 'http://localhost:8080/api';

function getToken() { return localStorage.getItem('token'); }
function getUsername() { return localStorage.getItem('username'); }
function getUserId() { return localStorage.getItem('userId'); }

function authHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + getToken()
  };
}

function isLoggedIn() {
  return !!getToken();
}

function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  localStorage.removeItem('userId');
  window.location.href = 'login.html';
}

// AUTH
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

// BLOGS
async function getBlogs(search = '', categoryId = '') {
  const res = await fetch(`${BASE}/blogs?search=${search}&categoryId=${categoryId}`);
  return res.json();
}

async function getBlog(id) {
  return (await fetch(`${BASE}/blogs/${id}`)).json();
}

async function createBlog(title, content, categoryId, excerpt) {
  const res = await fetch(`${BASE}/blogs`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ title, content, categoryId, excerpt })
  });
  return res.json();
}

async function deleteBlog(id) {
  const res = await fetch(`${BASE}/blogs/${id}`, {
    method: 'DELETE',
    headers: authHeaders()
  });
  return res.json();
}

// USERS
async function getUserProfile(id) {
  return (await fetch(`${BASE}/users/${id}`, { headers: authHeaders() })).json();
}

async function updateProfile(data) {
  const res = await fetch(`${BASE}/users/profile`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(data)
  });
  return res.json();
}

// LIKES
async function likeBlog(blogId) {
  return (await fetch(`${BASE}/likes/${blogId}`, {
    method: 'POST',
    headers: authHeaders()
  })).json();
}

async function unlikeBlog(blogId) {
  return (await fetch(`${BASE}/likes/${blogId}`, {
    method: 'DELETE',
    headers: authHeaders()
  })).json();
}

// COMMENTS
async function getComments(blogId) {
  return (await fetch(`${BASE}/comments/blog/${blogId}`)).json();
}

async function postComment(blogId, content) {
  return (await fetch(`${BASE}/comments`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ blogId, content })
  })).json();
}

// FOLLOW
async function followUser(userId) {
  return (await fetch(`${BASE}/follows/${userId}`, {
    method: 'POST',
    headers: authHeaders()
  })).json();
}

async function unfollowUser(userId) {
  return (await fetch(`${BASE}/follows/${userId}`, {
    method: 'DELETE',
    headers: authHeaders()
  })).json();
}

// BOOKMARK
async function bookmarkBlog(blogId) {
  return (await fetch(`${BASE}/bookmarks/${blogId}`, {
    method: 'POST',
    headers: authHeaders()
  })).json();
}

async function unbookmarkBlog(blogId) {
  return (await fetch(`${BASE}/bookmarks/${blogId}`, {
    method: 'DELETE',
    headers: authHeaders()
  })).json();
}

async function getBookmarks() {
  return (await fetch(`${BASE}/bookmarks`, { headers: authHeaders() })).json();
}