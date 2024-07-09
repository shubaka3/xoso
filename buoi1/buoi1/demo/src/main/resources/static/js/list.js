$(document).ready(function () {
    $.ajax({
        url: 'http://localhost:8081/api/v1/books',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let trHTML = "";
            $.each(data, function(i, item) {
                trHTML +=
                    '<tr id="book-' + item.id + '">' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.title + '</td>' +
                    '<td>' + item.author + '</td>' +
                    '<td>' + item.price + '</td>' +
                    '<td>' + item.category + '</td>' +
                    '<td sec:authorize="hasAnyAuthority(\'ADMIN\')">' +
                    '<a href="/home/api/edit/' + item.id + '" class="text-primary">Edit</a> | ' +
                    '<a href="/books" id="delete-' + item.id + '" class="text-danger" onclick="apiDeleteBook(' + item.id + ', this); return false;" sec:authorize="hasAnyAuthority(\'ADMIN\')">Delete</a>' +
                    '</td>' +
                    '</tr>';
            });
            $('#book-table-body').append(trHTML);
        },
        error: function (xhr, status, error) {
            console.error('Failed to fetch books:', status, error);
        }
    });
});

function apiDeleteBook(id) {
    if(confirm("Are you sure you want to delete this book?")){
        $.ajax(
            {
                url:'http://localhost:8081/api/v1/books/' + id,
                type: 'DELETE',
                success: function() {
                    alert('Book deleted successfully');
                    $('#book-' + id).remove();
                }
            }
        );
    }
}