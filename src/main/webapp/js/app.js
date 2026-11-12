/**
 * Littera - Funções de Interação e Validação Frontend
 */

document.addEventListener('DOMContentLoaded', function () {
    // Inicialização do Modal de Exclusão
    const btnAbrirModal = document.getElementById('btn-abrir-excluir');
    const modalExcluir = document.getElementById('modal-excluir');
    const btnCancelarModal = document.getElementById('btn-cancelar-modal');

    if (btnAbrirModal && modalExcluir) {
        btnAbrirModal.addEventListener('click', function (e) {
            e.preventDefault();
            modalExcluir.style.display = 'flex';
        });
    }

    if (btnCancelarModal && modalExcluir) {
        btnCancelarModal.addEventListener('click', function () {
            modalExcluir.style.display = 'none';
        });

        // Fechar ao clicar fora da caixa
        modalExcluir.addEventListener('click', function (e) {
            if (e.target === modalExcluir) {
                modalExcluir.style.display = 'none';
            }
        });
    }

    // Auto-dismiss em alertas após 5 segundos
    const alertas = document.querySelectorAll('.alert-feedback');
    if (alertas.length > 0) {
        setTimeout(function () {
            alertas.forEach(function (alerta) {
                alerta.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
                alerta.style.opacity = '0';
                alerta.style.transform = 'translateY(-10px)';
                setTimeout(function () {
                    alerta.remove();
                }, 400);
            });
        }, 5000);
    }

    // Validação nos formulários de cadastro e edição
    const formLivro = document.getElementById('form-livro');
    if (formLivro) {
        formLivro.addEventListener('submit', function (e) {
            const titulo = document.getElementById('titulo');
            const autor = document.getElementById('autor');
            const genero = document.getElementById('genero_id');

            let erros = [];

            if (!titulo || !titulo.value.trim()) {
                erros.push('Por favor, informe o título do livro.');
                titulo && titulo.focus();
            } else if (!autor || !autor.value.trim()) {
                erros.push('Por favor, informe o nome do autor.');
                autor && autor.focus();
            } else if (!genero || !genero.value) {
                erros.push('Por favor, selecione um gênero para o livro.');
                genero && genero.focus();
            }

            if (erros.length > 0) {
                e.preventDefault();
                alert(erros.join('\n'));
            }
        });
    }
});
