(() => {
    // Example: Scrape all text content from <p> tags
    const data = Array.from(document.querySelectorAll("span"))
      .map((el) => el.innerText)
      .join("\n");
  
    // Return the scraped data
    return data || "No <p> tags found.";
  })();