import { test, expect } from '@playwright/test';

test.describe('Smoke Tests', () => {
  
  test('Home page loads and has title', async ({ page }) => {
    await page.goto('/');
    await expect(page).toHaveTitle(/Justin Good/); // Adjust based on actual title
    await expect(page.locator('body')).toBeVisible();
  });

  test('Resume page loads', async ({ page }) => {
    await page.goto('/resume/'); // Trailing slash is often important in static sites
    await expect(page.locator('body')).toContainText(/Experience|Work|Skills/i);
  });

  test('RSS feed exists', async ({ page }) => {
    const response = await page.request.get('/rss.xml');
    expect(response.status()).toBe(200);
    const text = await response.text();
    expect(text).toContain('<?xml');
    expect(text).toContain('<rss');
  });

});
