document.getElementById("scrape-btn").addEventListener("click", async () => {
    // Inject the content script
    chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
      chrome.scripting.executeScript(
        {
          target: { tabId: tabs[0].id },
          files: ["content.js"]
        },
        (results) => {
          // Display the scraped data
          const output = document.getElementById("output");
          output.textContent = results[0].result || "No data scraped.";
        }
      );
    });
  });
  