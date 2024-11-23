// content.js
const puppeteer = require('puppeteer');
// Select all <span> elements whose ID starts with "caption-" followed by numbers
const captions = document.querySelectorAll('head');

const [el] = await document.$x('//*[@id="caption-"]');
const src = el.getProperty

// Extract the text content of each matching <span>
const captionTexts = Array.from(captions).map(span => span.textContent.trim());

// Log the extracted captions to the console
console.log("Scraped captions:", captionTexts);

// Optionally, send the data back to the background script or save it
chrome.runtime.sendMessage({ type: "captions", data: captionTexts });