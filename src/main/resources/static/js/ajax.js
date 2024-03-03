
let page = 1;

document.addEventListener('DOMContentLoaded', () => {
    const pageSize = 10;
    const loadMoreBtn = document.getElementById("load-more-btn");

    loadMoreBtn.addEventListener('click', () => {
        fetch(`/projects?page=${page}&size=${pageSize}`)
            .then(response => response.text()) // Convertir la respuesta a texto
            .then(html => {
                document.getElementById('projects-container').innerHTML += html; // Insertar el HTML en el contenedor
                page++; // Incrementar el número de página para la próxima carga
            })
            .catch(error => {
                console.error('Error fetching projects:', error);
            });
    });
});

let Recpage = 1;

document.addEventListener('DOMContentLoaded', () => {
    const pageSize = 10;
    const loadMoreBtn = document.getElementById("load-more-btn-rec");

    loadMoreBtn.addEventListener('click', () => {
        fetch(`/rec-projects?page=${Recpage}&size=${pageSize}`)
            .then(response => response.text()) // Convertir la respuesta a texto
            .then(html => {
                document.getElementById('projects-container').innerHTML += html; // Insertar el HTML en el contenedor
                Recpage++; // Incrementar el número de página para la próxima carga
            })
            .catch(error => {
                console.error('Error fetching projects:', error);
            });
    });
});




