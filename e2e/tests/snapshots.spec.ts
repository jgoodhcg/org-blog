import { test, expect } from '@playwright/test';

test.describe('Project Snapshots Stream', () => {

  test('Snapshots page loads', async ({ page }) => {
    // Go to the snapshots page
    await page.goto('/snapshots/');
    
    // Verify we are on the right page
    await expect(page.locator('h1')).toContainText('Stream');

    // NOTE: To test infinite scroll, we need at least 6 snapshot posts.
    // Uncomment and run when data is available.
    /*
    // Verify initial batch (Test Snapshots 6 down to 2)
    // Batch size is 5, so 6, 5, 4, 3, 2 should be there.
    await expect(page.locator('text=Test Snapshot 6')).toBeVisible();
    
    // Scroll to the bottom to trigger HTMX 'revealed'
    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));

    // Wait for the next batch to load
    await expect(page.locator('text=Test Snapshot 1')).toBeVisible();
    */
  });

});