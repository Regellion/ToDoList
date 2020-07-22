$(function(){

    const appendTask = function(data){
        var taskCode = '<a href="#" class="task-link" id="task-' + data.id + '" data-id="'+
        data.id + '">' + data.name + '</a>';
        $('#tasks-list')
            .append('<div data-id="'+ data.id +'">' + taskCode + '</div>');
    };

    //Show adding task form
    $('#show-add-task-form').click(function(){
        $('#task-form').css('display', 'flex');
    });

    //Closing adding task form
    $('#task-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Show edit task form
    $(document).on('click', '.edit-this-task', function(){
        $('#task-edit-form').css('display', 'flex');
    });

    //Closing edit task form
    $('#task-edit-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });


    //Getting task
    $(document).on('click', '.task-link', function(){
        var link = $(this);
        var taskId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/tasks/' + taskId,
            success: function(response)
            {
                var code = '<span><p>Описание дела: ' + response.description + '</p>' +
                '<p>Крайний срок выполнения: ' + response.date + '</p>' +
                '<button class="delete-task" name="'+ response.id +'">Удалить дело</button>' +
                '<button class="edit-this-task" name="' + response.id +'">Изменить дело</button></span>';
                link.parent().append(code);
            },
            error: function(response)
            {
                if(response.status === 404) {
                    alert('Дело не найдено!');
                }
            }
        });
        return false;
    });

    //Delete task
    $(document).on('click','.delete-task', function(){
        deleteTask(this.name);
    });

    deleteTask = function(taskId){
        $.ajax({
            method: "DELETE",
            url: '/tasks/' + taskId,
            success: function(response){
                $('div[data-id="' + taskId+'"]').remove();
            },
            error: function(response)
            {
                if(response.status === 404) {
                    alert('Дело не найдено!');
                }
            }
        });
    return false;
    };

    //Delete all tasks
    $('#delete-all-tasks').click(function()
    {
        $.ajax({
            method: "DELETE",
            url: '/tasks/',
            success: function(response)
            {
                $('div[data-id]').remove();
            },
            error: function(response){
                if(response.status === 404){
                    alert('Список дел пуст!');
                }
            }
        });
        return false;
    });

    //Edit task
    $(document).on('click', '.edit-this-task', function(){
        var taskId = this.name;
        $('#edit-task').click(function(){
            var data = $('#task-edit-form form').serialize();
            $.ajax({
                method: "PUT",
                url: '/tasks/' + taskId,
                data: data,
                success: function(response) {
                    $('#task-edit-form').css('display', 'none');
                    Location.reload();
                },
                error: function(response){
                    if(response.status === 404){
                        alert('Дело не найдено!');
                    }
                }
            });
        });
    });

    //Adding task
    $('#save-task').click(function()
    {
        var data = $('#task-form form').serialize();
        $.ajax({
            method: "POST",
            url: '/tasks/',
            data: data,
            success: function(response)
            {
                $('#task-form').css('display', 'none');
                var task = {};
                task.id = response;
                var dataArray = $('#task-form form').serializeArray();
                for(i in dataArray) {
                    task[dataArray[i]['name']] = dataArray[i]['value'];
                }
                appendTask(task);
            }
        });
        return false;
    });

});