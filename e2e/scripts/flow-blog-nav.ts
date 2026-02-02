import { chromium, Page } from '@playwright/test';
import path from 'path';
import fs from 'fs';

const BASE_URL = process.env.BASE_URL || 'http://localhost:8081';
const SCREENSHOT_DIR = path.join(__dirname, '../screenshots/flow-nav');

// Ensure output dir
if (!fs.existsSync(SCREENSHOT_DIR)) {
  fs.mkdirSync(SCREENSHOT_DIR, { recursive: true });
}

async function capture(page: Page, name: string) {
  const filepath = path.join(SCREENSHOT_DIR, `${name}.png`);
  await page.screenshot({ path: filepath });
  console.log(`[Checkpoint] ${name}: ${filepath}`);
}

async function run() {
  console.log('Starting Blog Navigation Flow...');
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();

  try {
    // 1. Visit Home
    console.log(`Visiting ${BASE_URL}`);
    await page.goto(BASE_URL, { waitUntil: 'domcontentloaded' });
    await capture(page, '01-home');

    // 2. Click the first blog post link
    // Assuming posts are listed in a way we can query. 
    // Based on standard blog structures, looking for an 'a' inside a post list or main content.
    // We'll try to find a link that looks like a post title.
    const postLink = page.locator('article a, .post-list a').first();
    
    if (await postLink.count() > 0) {
        const title = await postLink.innerText();
        console.log(`Clicking post: ${title}`);
        await postLink.click();
        await page.waitForLoadState('domcontentloaded');
        await capture(page, '02-post-detail');
    } else {
        console.warn('No post links found on homepage to click.');
    }

    // 3. Visit Archive (if exists)
    console.log('Visiting Archive...');
    await page.goto(`${BASE_URL}/archive/`, { waitUntil: 'domcontentloaded' });
    await capture(page, '03-archive');

  } catch (error) {
    console.error('Flow failed:', error);
    await capture(page, 'error-state');
    process.exit(1);
  } finally {
    await browser.close();
  }
}

run();
