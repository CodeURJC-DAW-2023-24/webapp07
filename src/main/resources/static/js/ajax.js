/*let page = 1;
document.addEventListener('DOMContentLoaded', (event) => {
     document.getElementById("load-more-btn").addEventListener('click', function (){
            fetch(`/projects?page=${page}&size=6`).then(response => response.json()).then(events => {
                let container = document.getElementById('projects-container');
                events.forEach(event => {
                    let div = document.createElement('div');
                    div.innerHTML = `
                <div class="col-lg-4 col-md-6 portfolio-item filter-web">
                <a href="project-details/${event.id}/">
                    <div class="portfolio-img"><img src="https://localhost:8443/projects/${event.id}/images/1" class="img-fluid" alt=""></div>
                    <div class="portfolio-info">
                        <h4>${event.name}</h4>
                        <p>${event.category}</p>
                        <a class="details-link" title="More Details">
                            <label class="ui-bookmark" style="position: absolute; top: 0; right: 0;">
                                <input type="checkbox">
                                <div class="bookmark">
                                    <svg viewBox="0 0 32 32">
                                        <g>
                                            <path d="M27 4v27a1 1 0 0 1-1.625.781L16 24.281l-9.375 7.5A1 1 0 0 1 5 31V4a4 4 0 0 1 4-4h14a4 4 0 0 1 4 4z"></path>
                                        </g>
                                    </svg>
                                </div>
                            </label>
                        </a>
                    </div>
                </a>
            `;
                    container.appendChild(div);
                })
                page++;
            })
        })
    })
*/

document.addEventListener('DOMContentLoaded', (event) => {
    let page = 1;

    document.getElementById("load-more-btn").addEventListener('click', function (){
        fetch(`/projects?page=${page}&size=6`).then(response => response.json()).then(events => {
            let container = document.getElementById('projects-container');

            // Vaciar el contenedor
            container.innerHTML = '';

            // Crear y agregar proyectos
            events.forEach(event => {
                let div = document.createElement('div');
                div.innerHTML = `
                    <div class="col-lg-4 col-md-6 portfolio-item filter-web">
                        <a href="project-details/${event.id}/">
                            <div class="portfolio-img"><img src="https://localhost:8443/projects/${event.id}/images/1" class="img-fluid" alt=""></div>
                            <div class="portfolio-info">
                                <h4>${event.name}</h4>
                                <p>${event.category}</p>
                                <a class="details-link" title="More Details">
                                    <label class="ui-bookmark" style="position: absolute; top: 0; right: 0;">
                                        <input type="checkbox">
                                        <div class="bookmark">
                                            <svg viewBox="0 0 32 32">
                                                <g>
                                                    <path d="M27 4v27a1 1 0 0 1-1.625.781L16 24.281l-9.375 7.5A1 1 0 0 1 5 31V4a4 4 0 0 1 4-4h14a4 4 0 0 1 4 4z"></path>
                                                </g>
                                            </svg>
                                        </div>
                                    </label>
                                </a>
                            </div>
                        </a>
                    </div>
                `;
                container.appendChild(div);
            });

            page++;
        });
    });
});

