chrome.action.onClicked.addListener((tab) => {
    chrome.scripting.executeScript({
      target: { tabId: tab.id },
      func: () => {
        const captions = document.querySelectorAll('head');
        const captionTexts = Array.from(captions).map(span => span.textContent.trim());
        console.log(captionTexts);
      }
    });
  });