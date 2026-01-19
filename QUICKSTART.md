# Quick Start Guide - Business Development Platform Suite

Get started in under 5 minutes!

---

## Choose Your Platform

### Option A: BWOP Business AI Platform (Instant - No Setup)

**Best for:** Immediate use, no technical setup required

**Steps:**

1. **Open the HTML file** in any modern browser:
   - Double-click `bwop-production-platform.html`
   - OR right-click → Open With → Your Browser

2. **Start using immediately:**
   - Upload documents
   - Search for grants
   - Generate proposals
   - Build business portfolios
   - Match loan opportunities

**That's it!** No installation, no configuration, no build process.

### Option B: DocuFlow Next.js App (Requires Node.js)

**Best for:** Developers who want to customize and extend

**Steps:**

1. **Install Node.js** (if not already installed):
   - Download from [nodejs.org](https://nodejs.org)
   - Choose LTS version (v18.17+)

2. **Navigate to project:**
   ```bash
   cd "Business Development/project"
   ```

3. **Install dependencies:**
   ```bash
   npm install
   ```

4. **Start development server:**
   ```bash
   npm run dev
   ```

5. **Open browser:**
   - Visit http://localhost:3000

---

## What Can I Do With Each Platform?

### BWOP Business AI Platform Features

| Feature | Description |
|---------|-------------|
| Document Scanner | Upload docs, extract fields, create templates |
| Grant Finder | Search federal, state, private grants |
| Proposal Writer | AI-generated business proposals |
| Portfolio Builder | Create professional business profiles |
| Loan Matcher | Find matching loan opportunities |
| Dashboard | Activity logs, data export, analytics |

### DocuFlow Features

| Feature | Description |
|---------|-------------|
| Document Management | Upload, organize, manage documents |
| Image Processing | Convert images to Base64 |
| Field Detection | Mock AI field detection system |
| Local Storage | All data stored in browser |
| Modern UI | Tailwind CSS + DaisyUI components |

---

## Testing the Platforms

### Test BWOP Platform

1. Open `bwop-production-platform.html` in browser
2. Click "Document Scanner" tab
3. Try uploading a test image
4. Click "Extract Fields" to see AI analysis
5. Explore other tabs (Grants, Proposals, Portfolio)

### Test DocuFlow

1. Start dev server: `npm run dev`
2. Click "Upload Document" button
3. Select an image file
4. Document should appear in the grid
5. Click "View" or "Delete" to test actions

---

## Deployment Quick Reference

### Deploy BWOP (HTML Platforms)

**GitHub Pages:**
```bash
# Commit files to git
git add .
git commit -m "Add BWOP platform"
git push origin main

# Enable GitHub Pages in repository settings
# Select branch: main, folder: / (root)
```

**Netlify (Drag & Drop):**
1. Go to [netlify.com](https://netlify.com)
2. Drag `bwop-production-platform.html` to deploy zone
3. Done!

**Traditional Hosting:**
1. Upload HTML files via FTP
2. Access via your domain

### Deploy DocuFlow

**Vercel (Recommended):**
```bash
npm i -g vercel
cd project
vercel
```

**Netlify:**
```bash
npm i -g netlify-cli
cd project
npm run build
netlify deploy --prod
```

---

## Common Questions

**Q: Do I need an internet connection?**
A: BWOP platforms work completely offline. DocuFlow needs internet only during `npm install`.

**Q: Where is my data stored?**
A: In your browser's LocalStorage. Data persists across sessions but stays on your device.

**Q: Can I use both platforms?**
A: Yes! They serve different purposes and can be used independently.

**Q: Is there a mobile version?**
A: Both platforms are responsive and work on mobile browsers.

**Q: Do I need a database?**
A: No! Both platforms use browser LocalStorage for data persistence.

**Q: Can I customize the platforms?**
A: Yes! The HTML files can be edited directly. DocuFlow can be modified by editing React components.

---

## Next Steps

### For BWOP Users

1. Explore all feature tabs
2. Create a business portfolio
3. Search for relevant grants
4. Generate your first proposal
5. Export your data (Dashboard → Export)

### For DocuFlow Developers

1. Read [project/README.md](project/README.md)
2. Explore the component structure
3. Customize the UI theme in `tailwind.config.js`
4. Add new components in `components/`
5. Deploy to Vercel or Netlify

---

## Troubleshooting

**BWOP Platform:**
- ❌ Blank page → Check browser console, enable JavaScript
- ❌ Upload not working → Check file size (<10MB) and format (JPG, PNG, PDF)
- ❌ Data disappeared → Check LocalStorage quota, export data regularly

**DocuFlow:**
- ❌ `npm install` fails → Delete `node_modules`, try `npm install --legacy-peer-deps`
- ❌ Port 3000 in use → Use `npm run dev -- -p 3001`
- ❌ Styles not showing → Clear `.next` folder, restart dev server

---

## Getting Help

1. Check the main [README.md](README.md)
2. Check platform-specific docs:
   - DocuFlow: [project/README.md](project/README.md)
3. Review browser console for errors
4. Ensure you meet prerequisites (Node.js 18.17+ for DocuFlow)

---

## File Locations

```
Business Development/
├── bwop-production-platform.html    ← Open this for BWOP
├── project/                         ← Navigate here for DocuFlow
├── README.md                        ← Full documentation
└── QUICKSTART.md                    ← You are here
```

---

**Ready to start?**

- **For instant use:** Open `bwop-production-platform.html`
- **For development:** `cd project && npm install && npm run dev`

---

**Built by SUDOCHOP LLC**
