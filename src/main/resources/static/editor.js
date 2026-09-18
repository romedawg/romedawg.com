// DOM Elements
const markdownSource = document.getElementById('markdownSource');
const postTitleInput = document.getElementById('postTitleInput');
const postAuthorInput = document.getElementById('postAuthorInput');
const postAuthorRoleInput = document.getElementById('postAuthorRoleInput');
const postCategorySelect = document.getElementById('postCategorySelect');
const postTagsInput = document.getElementById('postTagsInput');
const postSummaryInput = document.getElementById('postSummaryInput');

// Preview Elements
const previewTitle = document.getElementById('previewTitle');
const previewAuthor = document.getElementById('previewAuthor');
const previewMeta = document.getElementById('previewMeta');
const previewCategory = document.getElementById('previewCategory');
const previewBody = document.getElementById('previewBody');
const previewTags = document.getElementById('previewTags');
const previewReadTime = document.getElementById('previewReadTime');

// Stats Elements
const statWords = document.getElementById('statWords');
const statChars = document.getElementById('statChars');
const statLines = document.getElementById('statLines');
const statEstRead = document.getElementById('statEstRead');
const draftStatusText = document.getElementById('draftStatusText');

// View Controls
const workspace = document.getElementById('workspace');
const btnViewSplit = document.getElementById('btnViewSplit');
const btnViewEditor = document.getElementById('btnViewEditor');
const btnViewPreview = document.getElementById('btnViewPreview');

// Actions & Menus
const btnTemplates = document.getElementById('btnTemplates');
const templatesMenu = document.getElementById('templatesMenu');
const btnImportMd = document.getElementById('btnImportMd');
const mdFileInput = document.getElementById('mdFileInput');
const btnExportMd = document.getElementById('btnExportMd');
const btnPublish = document.getElementById('btnPublish');
const btnClearDraft = document.getElementById('btnClearDraft');
const btnHelp = document.getElementById('btnHelp');
const helpModalBackdrop = document.getElementById('helpModalBackdrop');
const closeHelpModalBtn = document.getElementById('closeHelpModalBtn');
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toastMessage');

// Templates Library
const TEMPLATES = {
  'tech-runbook': {
    title: 'Service Rollout & Architecture Migration Guide',
    author: 'Roman Rafacz',
    role: 'Staff SRE & Infrastructure',
    category: 'Engineering',
    tags: 'Architecture, Migration, SRE, Cloud',
    summary: 'A structured technical rollout runbook covering cluster topology, connection strings, security requirements, and verification checklists.',
    content: `### 1. Overview & Objectives
Provide a clear, high-level summary of the system architecture changes and operational goals for this rollout.

- **System / Portal**: [portal.internal.company.com](https://portal.internal.company.com)
- **Target Environments**: QA, UAT, Production
- **Primary Stakeholders**: SRE, Backend Engineering, QA, Operations

---

### 2. Connection Endpoints & Configuration

#### Application / Cluster Endpoints
Workloads connecting within VPC or managed clusters should use the corresponding endpoint URIs:

| Environment | Primary Endpoint URI | Auth / Secret Location |
|---|---|---|
| **QA** | \`service://qa-cluster-01.internal.net:8080\` | AWS SSM: \`/qa/services/db_creds\` |
| **UAT** | \`service://uat-cluster-01.internal.net:8080\` | AWS SSM: \`/uat/services/db_creds\` |
| **PROD** | \`service://prod-cluster-01.internal.net:8080\` | AWS SSM: \`/prod/services/db_creds\` |

---

### 3. Critical Security & TLS Requirements
Ensure secure TLS connectivity across all microservices:

\`\`\`yaml
# application-prod.yaml
security:
  tls:
    enabled: true
    protocol: TLSv1.3
    verify-peer: true
\`\`\`

> **Note**: Omitting TLS parameters in modern cluster topologies will trigger handshake timeouts during seed discovery.

---

### 4. Pre-Migration Checklist
- [x] Verify connectivity with individual service account secrets.
- [x] Provision target infrastructure in Terraform (\`terraform-infra-repo\`).
- [x] Stage rollback container images and pull requests.
- [x] Mute non-critical monitoring alarms during the maintenance window.

---

### 5. Execution Steps
1. **Quiesce Traffic**: Drain active connections or route to secondary replicas.
2. **Apply Changeset**: Merge the application configuration PR and deploy container.
3. **Validate Observability**: Review real-time APM metrics and transaction logs.

---

### 6. Verification & Observability Queries
Validate cluster health using the following log queries:

\`\`\`splunk
index="*-prod" source="*" AND "ConnectionEstablished" AND status=200
\`\`\`

---

### 7. Rollback Strategy
If critical latency thresholds or error rates are exceeded:
- Revert the configuration PR.
- Redeploy the previous verified container version.
- Re-enable legacy routing rules.`
  },
  'deep-dive': {
    title: 'Engineering Deep Dive: Building Scalable Event-Driven Architectures',
    author: 'Roman Rafacz',
    role: 'Principal Architect',
    category: 'Engineering',
    tags: 'Java, SpringBoot, Kafka, HighPerformance',
    summary: 'A comprehensive technical exploration of event sourcing, low-latency queues, and backpressure handling in enterprise distributed systems.',
    content: `### 1. The Challenge of Distributed Coordination
As systems scale across multiple availability zones, synchronous REST communication often becomes the bottleneck for latency and fault isolation.

### 2. Architectural Blueprint
By adopting an event-driven model, we decouple transaction ingestion from downstream processing:

\`\`\`java
@Component
public class EventPublisher {
    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, DomainEvent>> publish(DomainEvent event) {
        return kafkaTemplate.send("events-topic", event.getKey(), event);
    }
}
\`\`\`

### 3. Tradeoffs & Benchmarks
- **Throughput**: Achieved **45,000 req/sec** sustained with sub-10ms p99 latency.
- **Resilience**: Downstream failures no longer degrade the primary ingestion API.
- **Complexity**: Requires idempotent consumers and distributed tracing.`
  },
  'standard-post': {
    title: 'The Art of Simplicity in Modern Software',
    author: 'Roman',
    role: 'Chief Curiosity Officer',
    category: 'Philosophy',
    tags: 'Simplicity, Craft, Productivity',
    summary: 'Why removing unnecessary layers often delivers faster, more maintainable, and deeply satisfying software experiences.',
    content: `### The Clutter Trap
It is tempting to add libraries, frameworks, and abstractions before you truly understand the problem.

### 3 Principles for Clean Craft
1. **Start with the minimal working core**: Build only what moves the needle today.
2. **Write for humans first**: Code is read ten times more often than it is written.
3. **Celebrate deletion**: The best code is the code you don't have to maintain.

> *"Simplicity is prerequisite for reliability."* — Edsger W. Dijkstra`
  }
};

const DRAFT_STORAGE_KEY = 'the_curious_ape_editor_draft_v2';

// Edit mode state
let currentEditId = null;

// Initialization
document.addEventListener('DOMContentLoaded', () => {
  setupEventListeners();
  loadArticlesBrowser();

  const params = new URLSearchParams(window.location.search);
  const editId = params.get('id');
  if (editId) {
    loadArticleForEditing(editId);
  } else {
    loadSavedDraft();
  }
  updateLivePreviewAndStats();
});

// Setup All Listeners
function setupEventListeners() {
  // Input triggers for live preview & auto-save
  markdownSource.addEventListener('input', () => {
    updateLivePreviewAndStats();
    saveDraft();
  });

  [postTitleInput, postAuthorInput, postAuthorRoleInput, postCategorySelect, postTagsInput, postSummaryInput]
    .forEach(el => el.addEventListener('input', () => {
      updateLivePreviewAndStats();
      saveDraft();
    }));

  // Keyboard Shortcuts inside Editor Textarea
  markdownSource.addEventListener('keydown', handleEditorKeydown);

  // View Mode Selector
  btnViewSplit.addEventListener('click', () => setViewMode('split'));
  btnViewEditor.addEventListener('click', () => setViewMode('editor'));
  btnViewPreview.addEventListener('click', () => setViewMode('preview'));

  // Toolbar Buttons
  document.querySelectorAll('.tool-btn[data-action]').forEach(btn => {
    btn.addEventListener('click', () => {
      const action = btn.getAttribute('data-action');
      applyToolbarAction(action);
    });
  });

  // Template Dropdown
  btnTemplates.addEventListener('click', (e) => {
    e.stopPropagation();
    templatesMenu.classList.toggle('hidden');
  });

  document.addEventListener('click', () => {
    templatesMenu.classList.add('hidden');
  });

  document.querySelectorAll('.dropdown-item[data-template]').forEach(item => {
    item.addEventListener('click', () => {
      const templateKey = item.getAttribute('data-template');
      loadTemplate(templateKey);
    });
  });

  // Import / Export
  btnImportMd.addEventListener('click', () => mdFileInput.click());
  mdFileInput.addEventListener('change', handleFileImport);
  btnExportMd.addEventListener('click', handleFileExport);

  // Publish / Update
  btnPublish.addEventListener('click', handlePublish);

  // Articles browser toggle
  const btnToggleArticles = document.getElementById('btnToggleArticles');
  const btnRefreshArticles = document.getElementById('btnRefreshArticles');
  if (btnToggleArticles) {
    btnToggleArticles.addEventListener('click', () => {
      workspace.classList.toggle('browser-open');
    });
  }
  if (btnRefreshArticles) {
    btnRefreshArticles.addEventListener('click', loadArticlesBrowser);
  }

  // Reset Draft
  btnClearDraft.addEventListener('click', handleClearDraft);

  // Help Modal
  btnHelp.addEventListener('click', () => helpModalBackdrop.classList.remove('hidden'));
  closeHelpModalBtn.addEventListener('click', () => helpModalBackdrop.classList.add('hidden'));
  helpModalBackdrop.addEventListener('click', (e) => {
    if (e.target === helpModalBackdrop) helpModalBackdrop.classList.add('hidden');
  });
}

// View Mode Handler
function setViewMode(mode) {
  [btnViewSplit, btnViewEditor, btnViewPreview].forEach(b => b.classList.remove('active'));
  workspace.classList.remove('split-mode', 'editor-mode', 'preview-mode');

  if (mode === 'split') {
    btnViewSplit.classList.add('active');
    workspace.classList.add('split-mode');
  } else if (mode === 'editor') {
    btnViewEditor.classList.add('active');
    workspace.classList.add('editor-mode');
  } else if (mode === 'preview') {
    btnViewPreview.classList.add('active');
    workspace.classList.add('preview-mode');
  }
}

// Keyboard Shortcut & Tab Indentation Handling
function handleEditorKeydown(e) {
  const isMac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;
  const ctrlKey = isMac ? e.metaKey : e.ctrlKey;

  // Tab key: Insert 2 spaces
  if (e.key === 'Tab') {
    e.preventDefault();
    const start = markdownSource.selectionStart;
    const end = markdownSource.selectionEnd;
    const value = markdownSource.value;

    if (!e.shiftKey) {
      markdownSource.value = value.substring(0, start) + '  ' + value.substring(end);
      markdownSource.selectionStart = markdownSource.selectionEnd = start + 2;
    } else {
      // Outdent
      if (start >= 2 && value.substring(start - 2, start) === '  ') {
        markdownSource.value = value.substring(0, start - 2) + value.substring(start);
        markdownSource.selectionStart = markdownSource.selectionEnd = start - 2;
      }
    }
    updateLivePreviewAndStats();
    saveDraft();
    return;
  }

  // Ctrl / Cmd + B (Bold)
  if (ctrlKey && e.key.toLowerCase() === 'b') {
    e.preventDefault();
    applyToolbarAction('bold');
    return;
  }

  // Ctrl / Cmd + I (Italic)
  if (ctrlKey && e.key.toLowerCase() === 'i') {
    e.preventDefault();
    applyToolbarAction('italic');
    return;
  }

  // Ctrl / Cmd + E (Inline Code)
  if (ctrlKey && e.key.toLowerCase() === 'e') {
    e.preventDefault();
    applyToolbarAction('code-inline');
    return;
  }

  // Ctrl / Cmd + K (Link)
  if (ctrlKey && e.key.toLowerCase() === 'k') {
    e.preventDefault();
    applyToolbarAction('link');
    return;
  }
}

// Apply Toolbar Markdown Inserters
function applyToolbarAction(action) {
  const start = markdownSource.selectionStart;
  const end = markdownSource.selectionEnd;
  const text = markdownSource.value;
  const selected = text.substring(start, end);

  let replacement = '';
  let cursorOffset = 0;

  switch (action) {
    case 'h1':
      replacement = `# ${selected || 'Heading 1'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'h2':
      replacement = `## ${selected || 'Heading 2'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'h3':
      replacement = `### ${selected || 'Heading 3'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'bold':
      replacement = `**${selected || 'bold text'}**`;
      cursorOffset = selected ? replacement.length : 2;
      break;
    case 'italic':
      replacement = `*${selected || 'italic text'}*`;
      cursorOffset = selected ? replacement.length : 1;
      break;
    case 'strikethrough':
      replacement = `~~${selected || 'strikethrough text'}~~`;
      cursorOffset = selected ? replacement.length : 2;
      break;
    case 'code-inline':
      replacement = `\`${selected || 'code'}\``;
      cursorOffset = selected ? replacement.length : 1;
      break;
    case 'code-block':
      replacement = `\`\`\`javascript\n${selected || '// code block'}\n\`\`\`\n`;
      cursorOffset = replacement.length;
      break;
    case 'quote':
      replacement = `> ${selected || 'Important note or quote'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'table':
      replacement = `| Column 1 | Column 2 | Column 3 |\n|---|---|---|\n| Value A | Value B | Value C |\n| Value X | Value Y | Value Z |\n\n`;
      cursorOffset = replacement.length;
      break;
    case 'ul':
      replacement = `- ${selected || 'List item'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'ol':
      replacement = `1. ${selected || 'Numbered item'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'checklist':
      replacement = `- [ ] ${selected || 'Checklist task item'}\n`;
      cursorOffset = replacement.length;
      break;
    case 'hr':
      replacement = `\n---\n\n`;
      cursorOffset = replacement.length;
      break;
    case 'link':
      replacement = `[${selected || 'Link Title'}](https://example.com)`;
      cursorOffset = replacement.length;
      break;
    case 'image':
      replacement = `![${selected || 'Image Description'}](images/monkey.jpg)\n`;
      cursorOffset = replacement.length;
      break;
  }

  markdownSource.value = text.substring(0, start) + replacement + text.substring(end);
  markdownSource.focus();
  markdownSource.selectionStart = markdownSource.selectionEnd = start + cursorOffset;

  updateLivePreviewAndStats();
  saveDraft();
}

// Live Preview & Statistics Calculation
function updateLivePreviewAndStats() {
  const title = postTitleInput.value.trim() || 'Untitled Story';
  const author = postAuthorInput.value.trim() || 'Roman Rafacz';
  const authorRole = postAuthorRoleInput.value.trim() || 'Staff SRE';
  const category = postCategorySelect.value || 'Engineering';
  const tagsStr = postTagsInput.value.trim();
  const content = markdownSource.value;

  // Preview header fields
  previewTitle.textContent = title;
  previewAuthor.textContent = author;
  previewCategory.textContent = category;
  
  // Read time & word stats
  const words = content.trim() ? content.trim().split(/\s+/).length : 0;
  const chars = content.length;
  const lines = content ? content.split('\n').length : 0;
  const estMinutes = Math.max(1, Math.ceil(words / 180));
  const readTimeStr = `${estMinutes} min read`;

  previewMeta.textContent = `${authorRole} • Today • ${readTimeStr}`;
  previewReadTime.textContent = readTimeStr;

  // Stats bar
  statWords.textContent = `${words.toLocaleString()} words`;
  statChars.textContent = `${chars.toLocaleString()} characters`;
  statLines.textContent = `${lines.toLocaleString()} lines`;
  statEstRead.textContent = readTimeStr;

  // Render markdown body
  previewBody.innerHTML = formatMarkdown(content);

  // Render tags
  const tags = tagsStr.split(',').map(t => t.trim()).filter(Boolean);
  if (tags.length > 0) {
    previewTags.innerHTML = tags.map(t => `<span class="tag-badge">#${escapeHtml(t)}</span>`).join('');
  } else {
    previewTags.innerHTML = '<span class="tag-badge">#General</span>';
  }
}

// Format Markdown to HTML
function formatMarkdown(content) {
  if (!content) return '<p class="text-muted"><em>Your rendered story preview will appear here in real-time...</em></p>';

  // Preserve fenced code blocks
  const codeBlocks = [];
  let text = content.replace(/```([a-zA-Z0-9_-]*)\n([\s\S]*?)```/g, (match, lang, code) => {
    const placeholder = `__CODE_BLOCK_${codeBlocks.length}__`;
    codeBlocks.push(`<pre><code class="language-${escapeHtml(lang)}">${escapeHtml(code.trim())}</code></pre>`);
    return placeholder;
  });

  text = escapeHtml(text);

  codeBlocks.forEach((block, i) => {
    text = text.replace(`__CODE_BLOCK_${i}__`, block);
  });

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

    if (trimmed.startsWith('<pre><code') || trimmed.endsWith('</code></pre>') || trimmed.startsWith('<img')) {
      closeOpenStructures();
      result.push(rawLine);
      continue;
    }

    if (trimmed.startsWith('#### ')) {
      closeOpenStructures();
      result.push(`<h4>${trimmed.substring(5)}</h4>`);
      continue;
    }
    if (trimmed.startsWith('### ')) {
      closeOpenStructures();
      result.push(`<h3>${trimmed.substring(4)}</h3>`);
      continue;
    }
    if (trimmed.startsWith('## ')) {
      closeOpenStructures();
      result.push(`<h2>${trimmed.substring(3)}</h2>`);
      continue;
    }
    if (trimmed.startsWith('# ')) {
      closeOpenStructures();
      result.push(`<h1>${trimmed.substring(2)}</h1>`);
      continue;
    }

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

  return result.join('\n');
}

// Load Template
function loadTemplate(key) {
  if (key === 'clear') {
    if (confirm('Clear the current editor and start with a blank draft?')) {
      postTitleInput.value = '';
      markdownSource.value = '';
      postSummaryInput.value = '';
      updateLivePreviewAndStats();
      saveDraft();
      showToast('🧹 Editor cleared to blank slate');
    }
    return;
  }

  const tmpl = TEMPLATES[key];
  if (!tmpl) return;

  if (markdownSource.value.trim().length > 0) {
    if (!confirm('Loading this template will replace your current editor draft. Proceed?')) {
      return;
    }
  }

  postTitleInput.value = tmpl.title;
  postAuthorInput.value = tmpl.author;
  postAuthorRoleInput.value = tmpl.role;
  postCategorySelect.value = tmpl.category;
  postTagsInput.value = tmpl.tags;
  postSummaryInput.value = tmpl.summary;
  markdownSource.value = tmpl.content;

  updateLivePreviewAndStats();
  saveDraft();
  showToast(`📋 Loaded "${tmpl.title}" template!`);
}

// Auto-Save Draft to LocalStorage
function saveDraft() {
  const draft = {
    title: postTitleInput.value,
    author: postAuthorInput.value,
    authorRole: postAuthorRoleInput.value,
    category: postCategorySelect.value,
    tags: postTagsInput.value,
    summary: postSummaryInput.value,
    content: markdownSource.value,
    timestamp: new Date().toISOString()
  };

  try {
    localStorage.setItem(DRAFT_STORAGE_KEY, JSON.stringify(draft));
    draftStatusText.textContent = 'Draft auto-saved';
  } catch (err) {
    console.error('Error saving draft to localStorage:', err);
  }
}

// Load Saved Draft
function loadSavedDraft() {
  try {
    const saved = localStorage.getItem(DRAFT_STORAGE_KEY);
    if (saved) {
      const draft = JSON.parse(saved);
      if (draft.content || draft.title) {
        postTitleInput.value = draft.title || '';
        postAuthorInput.value = draft.author || 'Roman Rafacz';
        postAuthorRoleInput.value = draft.authorRole || 'Staff SRE & Infrastructure';
        postCategorySelect.value = draft.category || 'Engineering';
        postTagsInput.value = draft.tags || 'Cloud, SRE';
        postSummaryInput.value = draft.summary || '';
        markdownSource.value = draft.content || '';
        draftStatusText.textContent = 'Draft restored from storage';
        return;
      }
    }
  } catch (err) {
    console.error('Error reading draft:', err);
  }

  // Default initial content if no draft exists
  loadTemplate('tech-runbook');
}

// Reset Draft
function handleClearDraft() {
  if (confirm('Are you sure you want to delete your saved draft?')) {
    localStorage.removeItem(DRAFT_STORAGE_KEY);
    postTitleInput.value = '';
    markdownSource.value = '';
    postSummaryInput.value = '';
    updateLivePreviewAndStats();
    showToast('🗑️ Draft reset successfully');
  }
}

// Import .md File
function handleFileImport(e) {
  const file = e.target.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = (event) => {
    const content = event.target.result;
    const fileNameWithoutExt = file.name.replace(/\.[^/.]+$/, '');
    
    if (!postTitleInput.value.trim()) {
      postTitleInput.value = fileNameWithoutExt.replace(/[-_]/g, ' ');
    }
    markdownSource.value = content;
    updateLivePreviewAndStats();
    saveDraft();
    showToast(`📂 Imported "${file.name}" (${(file.size / 1024).toFixed(1)} KB)`);
  };
  reader.readAsText(file);
  mdFileInput.value = '';
}

// Export / Download .md File
function handleFileExport() {
  const title = postTitleInput.value.trim() || 'blog-post';
  const filename = `${title.toLowerCase().replace(/[^a-z0-9]+/g, '-')}.md`;
  const content = markdownSource.value;

  const blob = new Blob([content], { type: 'text/markdown;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);

  showToast(`💾 Exported "${filename}"`);
}

// Publish Story to Backend
async function handlePublish() {
  const title = postTitleInput.value.trim();
  const author = postAuthorInput.value.trim() || 'Roman Rafacz';
  const authorRole = postAuthorRoleInput.value.trim() || 'Staff SRE';
  const category = postCategorySelect.value || 'Engineering';
  const tagsStr = postTagsInput.value.trim();
  const summary = postSummaryInput.value.trim();
  const content = markdownSource.value.trim();

  if (!title) {
    alert('Please enter a title for your blog post.');
    postTitleInput.focus();
    return;
  }

  if (!content) {
    alert('Please write some markdown content before publishing.');
    markdownSource.focus();
    return;
  }

  const tags = tagsStr.split(',').map(t => t.trim()).filter(Boolean);

  const payload = {
    title,
    author,
    authorRole,
    category,
    tags: tags.length ? tags : ['General'],
    summary,
    content
  };

  const isEditing = currentEditId !== null;
  btnPublish.disabled = true;
  btnPublish.innerHTML = isEditing ? '<span>Saving... ⏳</span>' : '<span>Publishing... ⏳</span>';

  try {
    const url = isEditing ? `/api/posts/${currentEditId}` : '/api/posts';
    const method = isEditing ? 'PUT' : 'POST';

    const res = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error(`HTTP error ${res.status}`);

    localStorage.removeItem(DRAFT_STORAGE_KEY);

    showToast(isEditing ? '✅ Article updated! Redirecting...' : '🎉 Story published! Redirecting...');
    setTimeout(() => { window.location.href = 'index.html'; }, 1200);
  } catch (err) {
    console.error('Error saving story:', err);
    alert('Failed to save story. Please check the backend connection.');
    btnPublish.disabled = false;
    btnPublish.innerHTML = isEditing ? '<span>Update Story ✏️</span>' : '<span>Publish Story 🚀</span>';
  }
}

// Load an existing article into the editor for editing
async function loadArticleForEditing(id) {
  try {
    const res = await fetch(`/api/posts/${id}`);
    if (!res.ok) return;
    const data = await res.json();
    const post = data.post || data;

    currentEditId = String(id);
    postTitleInput.value = post.title || '';
    postAuthorInput.value = post.author || '';
    postAuthorRoleInput.value = post.authorRole || '';
    postCategorySelect.value = post.category || 'Articles';
    postTagsInput.value = (post.tags || []).join(', ');
    postSummaryInput.value = post.summary || '';
    markdownSource.value = post.content || '';

    btnPublish.innerHTML = '<span>Update Story ✏️</span>';
    draftStatusText.textContent = `Editing: ${post.title}`;

    updateLivePreviewAndStats();
    highlightActiveBrowserItem(currentEditId);
  } catch (e) {
    console.error('Failed to load article for editing:', e);
  }
}

// Load the articles browser panel
async function loadArticlesBrowser() {
  const list = document.getElementById('articlesList');
  if (!list) return;
  list.innerHTML = '<div class="articles-loading">Loading…</div>';
  try {
    const res = await fetch('/api/posts');
    const data = await res.json();
    const posts = data.posts || [];
    if (!posts.length) {
      list.innerHTML = '<div class="articles-loading">No articles yet.</div>';
      return;
    }
    list.innerHTML = posts.map(p => `
      <div class="article-browser-item ${currentEditId === String(p.id) ? 'active' : ''}"
           data-id="${p.id}" onclick="loadArticleForEditing('${p.id}')">
        <div class="ab-title">${escapeHtml(p.title)}</div>
        <div class="ab-meta">${p.category || ''} · ${p.date || ''}</div>
      </div>
    `).join('');
  } catch (e) {
    list.innerHTML = '<div class="articles-loading">Failed to load.</div>';
  }
}

function highlightActiveBrowserItem(id) {
  document.querySelectorAll('.article-browser-item').forEach(el => {
    el.classList.toggle('active', el.dataset.id === String(id));
  });
}

// Utilities
function escapeHtml(str) {
  if (!str) return '';
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

function showToast(msg) {
  toastMessage.textContent = msg;
  toast.classList.remove('hidden');
  setTimeout(() => {
    toast.classList.add('hidden');
  }, 3500);
}
