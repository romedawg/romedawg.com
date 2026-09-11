// Application State
let allPosts = [];
let currentCategory = 'home';
let searchQuery = '';
let currentArticleId = null;
let activeHeadingId = null;

// DOM Elements - Main Layout
const feedView = document.getElementById('feedView');
const aboutView = document.getElementById('aboutView');
const articleReaderView = document.getElementById('articleReaderView');
const postsStream = document.getElementById('postsStream');
const featuredPostContainer = document.getElementById('featuredPostContainer');
const emptyState = document.getElementById('emptyState');
const feedTitle = document.getElementById('feedTitle');
const feedDesc = document.getElementById('feedDesc');
const resultsCount = document.getElementById('resultsCount');

// DOM Elements - Navigation & Search
const navItems = document.querySelectorAll('.nav-item[data-nav]');
const searchInput = document.getElementById('searchInput');
const clearSearchBtn = document.getElementById('clearSearchBtn');
const resetFiltersBtn = document.getElementById('resetFiltersBtn');
const mobileMenuBtn = document.getElementById('mobileMenuBtn');
const leftSidebar = document.getElementById('leftSidebar');

// DOM Elements - Right Sidebar
const recentlyUpdatedList = document.getElementById('recentlyUpdatedList');
const countArticles = document.getElementById('countArticles');
const countRecipes = document.getElementById('countRecipes');
const countSwim = document.getElementById('countSwim');
const countSnowboarding = document.getElementById('countSnowboarding');

// DOM Elements - Article Reader (greennode.ai style)
const articleTitle = document.getElementById('articleTitle');
const articleAuthor = document.getElementById('articleAuthor');
const articleMeta = document.getElementById('articleMeta');
const articleCategoryBadge = document.getElementById('articleCategoryBadge');
const articleMarkdownBody = document.getElementById('articleMarkdownBody');
const articleTagsWrap = document.getElementById('articleTagsWrap');
const articleLikeBtn = document.getElementById('articleLikeBtn');
const articleLikeCount = document.getElementById('articleLikeCount');
const articleCrumbCategory = document.getElementById('articleCrumbCategory');
const articleCrumbTitle = document.getElementById('articleCrumbTitle');
const tocNav = document.getElementById('tocNav');
const btnShareStory = document.getElementById('btnShareStory');
const btnBottomShare = document.getElementById('btnBottomShare');

// Toast
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toastMessage');

// Initial Load
document.addEventListener('DOMContentLoaded', () => {
  setupEventListeners();
  fetchPosts();
});

// Setup Event Listeners
function setupEventListeners() {
  // Navigation tabs (Home, Articles, Recipes, Swim, Snowboarding, About)
  navItems.forEach(item => {
    item.addEventListener('click', (e) => {
      e.preventDefault();
      const nav = item.getAttribute('data-nav');
      setCategory(nav);
      if (leftSidebar && leftSidebar.classList.contains('mobile-open')) {
        leftSidebar.classList.remove('mobile-open');
      }
    });
  });

  // Category pill links in right sidebar
  document.querySelectorAll('.cat-pill[data-nav]').forEach(pill => {
    pill.addEventListener('click', (e) => {
      e.preventDefault();
      const nav = pill.getAttribute('data-nav');
      setCategory(nav);
    });
  });

  // Search input
  if (searchInput) {
    searchInput.addEventListener('input', (e) => {
      searchQuery = e.target.value.trim().toLowerCase();
      if (clearSearchBtn) {
        clearSearchBtn.classList.toggle('hidden', searchQuery.length === 0);
      }
      renderFeed();
    });
  }

  if (clearSearchBtn) {
    clearSearchBtn.addEventListener('click', () => {
      if (searchInput) searchInput.value = '';
      searchQuery = '';
      clearSearchBtn.classList.add('hidden');
      renderFeed();
    });
  }

  if (resetFiltersBtn) {
    resetFiltersBtn.addEventListener('click', () => {
      setCategory('home');
    });
  }

  // Mobile menu toggle
  if (mobileMenuBtn) {
    mobileMenuBtn.addEventListener('click', () => {
      if (leftSidebar) leftSidebar.classList.toggle('mobile-open');
    });
  }

  // Share buttons
  if (btnShareStory) btnShareStory.addEventListener('click', () => handleShare());
  if (btnBottomShare) btnBottomShare.addEventListener('click', () => handleShare());

  // Like button in article reader
  if (articleLikeBtn) {
    articleLikeBtn.addEventListener('click', (e) => {
      if (currentArticleId) handleLike(e, currentArticleId);
    });
  }

  // Handle URL Hash Navigation
  window.addEventListener('hashchange', handleHashRouting);

  // ScrollSpy for Article Table of Contents
  window.addEventListener('scroll', handleScrollSpy, { passive: true });
}

// Fetch Posts from Backend
async function fetchPosts() {
  try {
    const res = await fetch('/api/posts');
    if (!res.ok) throw new Error('Failed to load posts');
    const data = await res.json();
    allPosts = data.posts || [];
    
    updateCategoryCounts();
    renderRecentlyUpdated();
    handleHashRouting();
  } catch (err) {
    console.error('Error fetching posts:', err);
    if (postsStream) {
      postsStream.innerHTML = `
        <div class="empty-state">
          <div class="empty-icon">⚠️</div>
          <h3>Unable to connect to backend</h3>
          <p>Make sure the Spring Boot server is running on port 8080.</p>
        </div>
      `;
    }
  }
}

// URL Hash Router
function handleHashRouting() {
  const hash = window.location.hash.replace('#', '') || 'home';

  if (hash.startsWith('article-')) {
    const id = hash.replace('article-', '');
    openArticle(id, false);
  } else if (['home', 'articles', 'recipes', 'swim', 'snowboarding', 'about'].includes(hash.toLowerCase())) {
    setCategory(hash.toLowerCase(), false);
  } else {
    setCategory('home', false);
  }
}

// Category Switcher
function setCategory(category, updateHash = true) {
  currentCategory = category;
  currentArticleId = null;
  searchQuery = '';
  if (searchInput) searchInput.value = '';
  if (clearSearchBtn) clearSearchBtn.classList.add('hidden');

  if (updateHash) {
    window.location.hash = category;
  }

  // Update active navigation item
  navItems.forEach(item => {
    item.classList.toggle('active', item.getAttribute('data-nav') === category);
  });

  // Toggle views
  if (category === 'about') {
    feedView.classList.add('hidden');
    aboutView.classList.remove('hidden');
    articleReaderView.classList.add('hidden');
    window.scrollTo({ top: 0, behavior: 'smooth' });
    return;
  }

  feedView.classList.remove('hidden');
  aboutView.classList.add('hidden');
  articleReaderView.classList.add('hidden');

  // Update titles & descriptions
  const descriptions = {
    'home': { title: 'All Stories', desc: 'Latest engineering runbooks, recipes, training logs, and ideas.' },
    'articles': { title: 'Articles & Engineering', desc: 'Technical architecture, Couchbase Capella migrations, and SRE runbooks.' },
    'recipes': { title: 'Kitchen & Coffee Recipes', desc: 'Pour-over coffee formulas, baking recipes, and fuel for deep work.' },
    'swim': { title: 'Swim & Open Water', desc: 'Open water sighting techniques, endurance lap workouts, and cold water tips.' },
    'snowboarding': { title: 'Snowboarding & Backcountry', desc: 'Powder boards, stance configuration, layering systems, and mountain safety.' }
  };

  const info = descriptions[category] || descriptions['home'];
  if (feedTitle) feedTitle.textContent = info.title;
  if (feedDesc) feedDesc.textContent = info.desc;

  renderFeed();
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Render Feed Stream
function renderFeed() {
  let filtered = [...allPosts];

  // Category filter
  if (currentCategory !== 'home') {
    filtered = filtered.filter(p => p.category && p.category.toLowerCase() === currentCategory.toLowerCase());
  }

  // Search filter
  if (searchQuery) {
    filtered = filtered.filter(p => {
      const inTitle = p.title && p.title.toLowerCase().includes(searchQuery);
      const inSummary = p.summary && p.summary.toLowerCase().includes(searchQuery);
      const inContent = p.content && p.content.toLowerCase().includes(searchQuery);
      const inTags = p.tags && p.tags.some(t => t.toLowerCase().includes(searchQuery));
      return inTitle || inSummary || inContent || inTags;
    });
  }

  if (resultsCount) {
    resultsCount.textContent = `${filtered.length} ${filtered.length === 1 ? 'story' : 'stories'}`;
  }

  // Empty state
  if (filtered.length === 0) {
    if (featuredPostContainer) featuredPostContainer.innerHTML = '';
    if (postsStream) postsStream.innerHTML = '';
    if (emptyState) emptyState.classList.remove('hidden');
    return;
  }

  if (emptyState) emptyState.classList.add('hidden');

  // Spotlight on Home view when no search active
  if (currentCategory === 'home' && !searchQuery && filtered.some(p => p.featured)) {
    const featured = filtered.find(p => p.featured);
    renderFeaturedSpotlight(featured);
    const regular = filtered.filter(p => p.id !== featured.id);
    renderStreamCards(regular);
  } else {
    if (featuredPostContainer) featuredPostContainer.innerHTML = '';
    renderStreamCards(filtered);
  }
}

// Render Featured Spotlight Card
function renderFeaturedSpotlight(post) {
  if (!featuredPostContainer || !post) return;
  featuredPostContainer.innerHTML = `
    <div class="featured-card" onclick="openArticle('${post.id}')">
      <div class="featured-meta">
        <span class="featured-badge">🌟 Featured Story</span>
        <span class="featured-category">${escapeHtml(post.category || 'Articles')}</span>
      </div>
      <h2 class="featured-title">${escapeHtml(post.title)}</h2>
      <p class="featured-excerpt">${escapeHtml(post.summary)}</p>
      <div class="featured-footer">
        <span class="recent-meta">${formatDate(post.date)} • ${escapeHtml(post.readTime || '5 min read')} • By ${escapeHtml(post.author)}</span>
        <button class="like-btn" onclick="handleLike(event, '${post.id}')">
          <span>❤️</span>
          <span id="spotlight-like-count-${post.id}">${post.likes || 0}</span>
        </button>
      </div>
    </div>
  `;
}

// Render Stream Cards
function renderStreamCards(posts) {
  if (!postsStream) return;
  postsStream.innerHTML = posts.map(post => {
    const catClass = `cat-${(post.category || 'Articles').replace(/\s+/g, '')}`;
    return `
      <article class="stream-card" onclick="openArticle('${post.id}')">
        <div class="stream-card-top">
          <span class="stream-category-badge ${catClass}">${escapeHtml(post.category || 'Articles')}</span>
          <span class="stream-date">${formatDate(post.date)}</span>
        </div>
        <h3 class="stream-title">${escapeHtml(post.title)}</h3>
        <p class="stream-excerpt">${escapeHtml(post.summary)}</p>
        <div class="stream-footer">
          <div class="stream-tags">
            ${(post.tags || []).map(t => `<span class="tag-badge">#${escapeHtml(t)}</span>`).join('')}
          </div>
          <div class="stream-meta-right">
            <span>${escapeHtml(post.readTime || '3 min read')}</span>
            <button class="like-btn" onclick="handleLike(event, '${post.id}')" title="Like story">
              <span>❤️</span>
              <span id="stream-like-count-${post.id}">${post.likes || 0}</span>
            </button>
          </div>
        </div>
      </article>
    `;
  }).join('');
}

// Render "Recently Updated" Sidebar Widget
function renderRecentlyUpdated() {
  if (!recentlyUpdatedList) return;
  const sorted = [...allPosts].sort((a, b) => new Date(b.date) - new Date(a.date)).slice(0, 5);
  
  recentlyUpdatedList.innerHTML = sorted.map(post => `
    <li class="recent-item" onclick="openArticle('${post.id}')">
      <span class="recent-title">${escapeHtml(post.title)}</span>
      <span class="recent-meta">${formatDate(post.date)} • ${escapeHtml(post.category || 'Articles')}</span>
    </li>
  `).join('');
}

// Update Category Counts in Right Sidebar
function updateCategoryCounts() {
  if (countArticles) {
    countArticles.textContent = allPosts.filter(p => p.category && p.category.toLowerCase() === 'articles').length;
  }
  if (countRecipes) {
    countRecipes.textContent = allPosts.filter(p => p.category && p.category.toLowerCase() === 'recipes').length;
  }
  if (countSwim) {
    countSwim.textContent = allPosts.filter(p => p.category && p.category.toLowerCase() === 'swim').length;
  }
  if (countSnowboarding) {
    countSnowboarding.textContent = allPosts.filter(p => p.category && p.category.toLowerCase() === 'snowboarding').length;
  }
}

// ==========================================================================
// ARTICLE READER (greennode.ai style layout with Table of Contents)
// ==========================================================================

function openArticle(id, updateHash = true) {
  const post = allPosts.find(p => p.id === id || p.slug === id);
  if (!post) return;

  currentArticleId = post.id;
  if (updateHash) {
    window.location.hash = `article-${post.id}`;
  }

  // Switch views
  feedView.classList.add('hidden');
  aboutView.classList.add('hidden');
  articleReaderView.classList.remove('hidden');

  // Populate Header & Metadata
  articleTitle.textContent = post.title;
  articleAuthor.textContent = post.author;
  articleCategoryBadge.textContent = post.category || 'Articles';
  articleCategoryBadge.className = `article-category-badge cat-${(post.category || 'Articles').replace(/\s+/g, '')}`;
  articleMeta.textContent = `${post.authorRole || 'Contributor'} • ${formatDate(post.date)} • ${post.readTime || '4 min read'}`;
  
  articleCrumbCategory.textContent = post.category || 'Articles';
  articleCrumbTitle.textContent = post.title;

  if (articleLikeCount) articleLikeCount.textContent = post.likes || 0;

  // Render Tags
  if (articleTagsWrap) {
    articleTagsWrap.innerHTML = (post.tags || []).map(t => `<span class="tag-badge">#${escapeHtml(t)}</span>`).join('');
  }

  // Parse Markdown & Generate Anchored Headings
  const { html, headings } = formatArticleMarkdown(post.content);
  articleMarkdownBody.innerHTML = html;

  // Render Table of Contents (TOC) on Left Sidebar
  renderTableOfContents(headings);

  // Setup Code Block Copy Buttons
  setupCodeCopyButtons();

  window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Return back to feed
function navigateFeed() {
  setCategory(currentCategory || 'home');
}

// Parse Markdown and extract headings for Table of Contents
function formatArticleMarkdown(content) {
  if (!content) return { html: '', headings: [] };

  const headings = [];
  let headingCounter = 0;

  // Preserve fenced code blocks
  const codeBlocks = [];
  let text = content.replace(/```([a-zA-Z0-9_-]*)\n([\s\S]*?)```/g, (match, lang, code) => {
    const placeholder = `__CODE_BLOCK_${codeBlocks.length}__`;
    codeBlocks.push({ lang: lang || 'code', code: code.trim() });
    return placeholder;
  });

  text = escapeHtml(text);

  // Inline Code
  text = text.replace(/`([^`]+)`/g, '<code>$1</code>');

  // Links & Images
  text = text.replace(/!\[([^\]]*)\]\((https?:\/\/[^\)]+|\/[^\)]+|[a-zA-Z0-9_\-\.\/]+)\)/g, '<img src="$2" alt="$1" style="max-width:100%; border-radius:12px; margin:1rem 0;" />');
  text = text.replace(/\[([^\]]+)\]\((https?:\/\/[^\)]+)\)/g, '<a href="$2" target="_blank" rel="noopener noreferrer">$1 ↗</a>');

  // Bold & Italic & Strikethrough
  text = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
  text = text.replace(/\*(.*?)\*/g, '<em>$1</em>');
  text = text.replace(/~~(.*?)~~/g, '<del>$1</del>');

  // Checkbox icons
  text = text.replace(/\[x\]/gi, '✅');
  text = text.replace(/\[ \]/g, '⬜');

  const lines = text.split('\n');
  const result = [];
  let inList = false;
  let inNumList = false;
  let inTable = false;

  for (let i = 0; i < lines.length; i++) {
    const rawLine = lines[i];
    const trimmed = rawLine.trim();

    if (trimmed === '---' || trimmed === '***') {
      closeOpenStructures();
      result.push('<hr />');
      continue;
    }

    // Code block placeholder
    if (trimmed.startsWith('__CODE_BLOCK_')) {
      closeOpenStructures();
      const index = parseInt(trimmed.replace('__CODE_BLOCK_', '').replace('__', ''));
      const block = codeBlocks[index];
      if (block) {
        result.push(`
          <div class="code-block-wrapper">
            <div class="code-block-header">
              <span>${escapeHtml(block.lang.toUpperCase())}</span>
              <button class="btn-copy-code" data-code="${escapeHtml(block.code)}">Copy</button>
            </div>
            <pre><code class="language-${escapeHtml(block.lang)}">${escapeHtml(block.code)}</code></pre>
          </div>
        `);
      }
      continue;
    }

    // Headings (H2 and H3 create TOC anchors)
    if (trimmed.startsWith('### ')) {
      closeOpenStructures();
      const title = trimmed.substring(4);
      const id = `heading-${headingCounter++}`;
      headings.push({ id, title, level: 3 });
      result.push(`<h3 id="${id}">${title}</h3>`);
      continue;
    }
    if (trimmed.startsWith('## ')) {
      closeOpenStructures();
      const title = trimmed.substring(3);
      const id = `heading-${headingCounter++}`;
      headings.push({ id, title, level: 2 });
      result.push(`<h2 id="${id}">${title}</h2>`);
      continue;
    }
    if (trimmed.startsWith('# ')) {
      closeOpenStructures();
      const title = trimmed.substring(2);
      const id = `heading-${headingCounter++}`;
      headings.push({ id, title, level: 1 });
      result.push(`<h2 id="${id}">${title}</h2>`);
      continue;
    }

    // Blockquote
    if (trimmed.startsWith('&gt; ') || trimmed.startsWith('> ')) {
      closeOpenStructures();
      const bq = trimmed.replace(/^(&gt;|>)\s*/, '');
      result.push(`<blockquote>${bq}</blockquote>`);
      continue;
    }

    // Markdown Table
    if (trimmed.startsWith('|') && trimmed.endsWith('|')) {
      if (trimmed.replace(/[\s|:-]/g, '').length === 0) continue;
      const cells = trimmed.split('|').slice(1, -1).map(c => c.trim());
      if (!inTable) {
        closeOpenStructures();
        inTable = true;
        result.push('<table><thead><tr>');
        cells.forEach(c => result.push(`<th>${c}</th>`));
        result.push('</tr></thead><tbody>');
      } else {
        result.push('<tr>');
        cells.forEach(c => result.push(`<td>${c}</td>`));
        result.push('</tr>');
      }
      continue;
    } else if (inTable) {
      result.push('</tbody></table>');
      inTable = false;
    }

    // Lists
    if (trimmed.startsWith('- ') || trimmed.startsWith('* ')) {
      if (inNumList) { result.push('</ol>'); inNumList = false; }
      if (!inList) { result.push('<ul>'); inList = true; }
      result.push(`<li>${trimmed.substring(2)}</li>`);
      continue;
    }

    if (trimmed.match(/^\d+\.\s/)) {
      if (inList) { result.push('</ul>'); inList = false; }
      if (!inNumList) { result.push('<ol>'); inNumList = true; }
      result.push(`<li>${trimmed.replace(/^\d+\.\s/, '')}</li>`);
      continue;
    }

    closeOpenStructures();
    if (trimmed.length > 0) {
      result.push(`<p>${trimmed}</p>`);
    }
  }

  closeOpenStructures();

  function closeOpenStructures() {
    if (inList) { result.push('</ul>'); inList = false; }
    if (inNumList) { result.push('</ol>'); inNumList = false; }
    if (inTable) { result.push('</tbody></table>'); inTable = false; }
  }

  return { html: result.join('\n'), headings };
}

// Render Table of Contents on Left Sidebar
function renderTableOfContents(headings) {
  if (!tocNav) return;

  if (headings.length === 0) {
    tocNav.innerHTML = '<span class="text-light" style="font-size:0.8rem;">No sections found</span>';
    return;
  }

  tocNav.innerHTML = headings.map(h => `
    <a href="#${h.id}" class="toc-link ${h.level === 3 ? 'toc-h3' : ''}" data-heading="${h.id}">
      ${escapeHtml(h.title)}
    </a>
  `).join('');

  // Add click handlers for smooth scroll
  tocNav.querySelectorAll('.toc-link').forEach(link => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      const targetId = link.getAttribute('data-heading');
      const targetEl = document.getElementById(targetId);
      if (targetEl) {
        targetEl.scrollIntoView({ behavior: 'smooth', block: 'start' });
        // Set active state
        tocNav.querySelectorAll('.toc-link').forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      }
    });
  });

  // Activate first heading by default
  const firstLink = tocNav.querySelector('.toc-link');
  if (firstLink) firstLink.classList.add('active');
}

// ScrollSpy: Highlight TOC heading based on viewport scroll position
function handleScrollSpy() {
  if (articleReaderView.classList.contains('hidden') || !tocNav) return;

  const headings = articleMarkdownBody.querySelectorAll('h1, h2, h3');
  if (!headings.length) return;

  let currentActive = null;
  const scrollPosition = window.scrollY + 120;

  headings.forEach(heading => {
    if (heading.offsetTop <= scrollPosition) {
      currentActive = heading.id;
    }
  });

  if (currentActive && currentActive !== activeHeadingId) {
    activeHeadingId = currentActive;
    tocNav.querySelectorAll('.toc-link').forEach(link => {
      const match = link.getAttribute('data-heading') === currentActive;
      link.classList.toggle('active', match);
    });
  }
}

// Setup Code Copy Buttons
function setupCodeCopyButtons() {
  document.querySelectorAll('.btn-copy-code').forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.stopPropagation();
      const code = btn.getAttribute('data-code');
      if (navigator.clipboard && code) {
        navigator.clipboard.writeText(code).then(() => {
          const originalText = btn.textContent;
          btn.textContent = 'Copied! ✅';
          setTimeout(() => { btn.textContent = originalText; }, 2000);
        });
      }
    });
  });
}

// Global functions for inline attributes
window.openArticle = openArticle;
window.navigateFeed = navigateFeed;
window.handleLike = handleLike;

// Handle Like Action
async function handleLike(event, id) {
  if (event) event.stopPropagation();
  try {
    const res = await fetch(`/api/posts/${id}/like`, { method: 'POST' });
    if (!res.ok) throw new Error('Failed to like post');
    const data = await res.json();
    
    // Update local state
    const post = allPosts.find(p => p.id === id);
    if (post) post.likes = data.likes;

    // Update UI counters
    const streamCount = document.getElementById(`stream-like-count-${id}`);
    if (streamCount) streamCount.textContent = data.likes;

    const spotlightCount = document.getElementById(`spotlight-like-count-${id}`);
    if (spotlightCount) spotlightCount.textContent = data.likes;

    if (currentArticleId === id && articleLikeCount) {
      articleLikeCount.textContent = data.likes;
    }

    if (event && event.currentTarget) {
      event.currentTarget.classList.add('liked');
    }
  } catch (err) {
    console.error('Error liking post:', err);
  }
}

// Share Story URL
function handleShare() {
  const url = window.location.href;
  if (navigator.clipboard) {
    navigator.clipboard.writeText(url);
    showToast('📋 Story link copied to clipboard!');
  } else {
    showToast('Story ready!');
  }
}

// Toast
function showToast(msg) {
  if (!toast || !toastMessage) return;
  toastMessage.textContent = msg;
  toast.classList.remove('hidden');
  setTimeout(() => { toast.classList.add('hidden'); }, 3000);
}

// Helper Utilities
function formatDate(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return dateStr;
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
}

function escapeHtml(str) {
  if (!str) return '';
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
