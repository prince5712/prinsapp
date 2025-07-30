    document.addEventListener('DOMContentLoaded', function() {
        // Function to apply theme based on user's preference
        function applyTheme() {
            const htmlElement = document.documentElement;

            // Check if the user's system prefers a dark or light color scheme
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                htmlElement.classList.add('prinsberwa-dark');
                htmlElement.classList.remove('light');
            } else {
                htmlElement.classList.add('light');
                htmlElement.classList.remove('prinsberwa-dark');
            }
        }

        // Apply theme on initial load
        applyTheme();

        // Listen for changes in the color scheme preference
        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', applyTheme);
    });
