import { chromium } from '@playwright/test';
import path from 'path';
import fs from 'fs';

// Helper to ensure directory exists
const ensureDir = (dir: string) => {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
};

async function run() {
  const args = process.argv.slice(2);
  const route = args[0] || '/';
  const isFullPage = args.includes('--full');

  const baseURL = process.env.BASE_URL || 'http://localhost:8081';
  const targetURL = `${baseURL}${route}`;

  console.log(`Navigating to ${targetURL}...`);

  const browser = await chromium.launch();
  const page = await browser.newPage();

  try {
    await page.goto(targetURL, { waitUntil: 'networkidle' });

    // Sanity check: verify we didn't get a 404 text body if the server is up but page missing
    // (Adjust selector based on your 404 page if specific)
    
    const screenshotDir = path.join(__dirname, '../screenshots');
    ensureDir(screenshotDir);

    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const safeRoute = route.replace(/\//g, '_') || '_root';
    const filename = `screenshot${safeRoute}-${timestamp}.png`;
    const filepath = path.join(screenshotDir, filename);

    await page.screenshot({ path: filepath, fullPage: isFullPage });

    console.log(`Screenshot saved: ${filepath}`);
  } catch (error) {
    console.error('Error taking screenshot:', error);
    process.exit(1);
  } finally {
    await browser.close();
  }
}

run();
