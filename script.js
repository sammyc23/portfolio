document.getElementById("contactForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const formData = {
    name: e.target.name.value,
    email: e.target.email.value
  };

  const response = await fetch("https://formspree.io/f/mreevzge", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(formData)
  });

  if (response.ok) {
    alert("Form submitted!");
  } else {
    alert("Submission failed");
  }
});
