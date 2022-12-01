let chatRoomList = document.querySelector('.list-unstyled')
let modal = document.querySelector('.modal-content')
let createButton = modal.querySelector('.btn-primary')
let cancelButton = modal.querySelector('.btn-secondary')
let chatArea = document.querySelector('#chatRoom')
let chatInputArea = document.querySelector('#chatInputArea')
let noChatArea = document.querySelector('#noChat')
let topChatArea = document.querySelector('#topChat')
const sendButton = document.querySelector('#sendButton')
let searchBar = document.querySelector('#searchBar')
let searchButton = searchBar.querySelector('.fa-search')
let lastResponse
let roomInfo = document.querySelector('#largeModal')
let usersInRoom = roomInfo.querySelector('.modal-body')
searchButton.addEventListener('click', function () {
    let searchInput = searchBar.querySelector('input').value.trim()
    if (searchInput === "") {
        return
    }
    var settings = {
        "url": "http://localhost:8080/rooms/search/findWithSearch?roomName=" + searchInput,
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
    };

    $.ajax(settings).done(function (response) {
        createAllRooms(response._embedded.rooms)
    });
})
let currentRoom

createButton.addEventListener('click', createNewRoom)

function createNewRoom() {
    let input = modal.querySelector('.form-control').value
    if (input === "") {

        return
    }
    const chatRoom = {
        roomName: input,
        owner: user
    }

    var settings = {
        "url": "http://localhost:8080/chat/create",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(chatRoom),
    };

    $.ajax(settings).done(function (response) {
        cancelButton.click()
        console.log(response);
        createRoom(response.rooms[response.rooms.length - 1])
    });

}


loadRooms()

function loadRooms() {

    var settings = {
        "url": "http://localhost:8080/users/" + user.id + "/rooms",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
    };

    $.ajax(settings).done(function (response) {
        createAllRooms(response._embedded.rooms)
    });
}


function createAllRooms(response) {
    chatRoomList.innerHTML = ''
    for (let i = 0; i < response.length; i++) {
        createRoom(response[i])
    }
}


function createRoom(response) {
    let src
    response.owner.avatar === null ? src = 'https://bootdey.com/img/Content/avatar/avatar6.png' : src = '\\image\\' + response.owner.avatar.id
    const room = document.createElement('li')
    room.classList.add("p-2", "border-bottom")
    room.innerHTML = "<a href=\"#!\" class=\"d-flex justify-content-between\">\n" +
        "        <div class=\"d-flex flex-row\">\n" +
        "            <div>\n" +
        "                <img\n" +
        "                    src=\"" + src + "\"" +
        "                    alt=\"avatar\" class=\"rounded-circle d-flex align-self-center me-3\" width=\"60\">\n" +
        "                    <span class=\"badge bg-success badge-dot\"></span>\n" +
        "            </div>\n" +
        "            <div class=\"pt-1\">\n" +
        "                <p class=\"fw-bold mb-0\">" + response.roomName + "</p>\n" +
        "                <p class=\"small text-muted\">Move to this chat</p>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"pt-1\">\n" +
        "            <p class=\"small text-muted mb-1\">Just now</p>\n" +
        "        </div>\n" +
        "    </a>"
    room.addEventListener('click', function () {
        showChat(response)
    })
    chatRoomList.appendChild(room)
}


function showChat(response) {
    var settings_1 = {
        "url": "http://localhost:8080/chat/check?id=" + response.id,
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
    };

    $.ajax(settings_1).done(function (response) {

    });

    var settings_2 = {
        "url": "http://localhost:8080/messages/search/messagesInChat?id=" + response.id + "&page=0&size=10&sort=sendTime,asc",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
    };


    if (currentRoom && stompClient) {
        stompClient.unsubscribe('/topic/' + currentRoom.id)
    }
    currentRoom = response

    $.ajax(settings_2).done(function (response) {
        connect()
        topChatArea.style.display = 'block'
        chatArea.style.display = 'block'
        chatInputArea.style.display = 'block'
        chatInputArea.classList.add('d-flex')
        noChatArea.style.display = 'none'
        chatArea.innerHTML = ''
        lastResponse = response

        customizeChatArea()
        showAllMessages(response._embedded.messages)
        chatArea.scrollTop = chatArea.scrollHeight
        // console.log(response);
        // createAllRooms(response)
    });
}

function showAllMessages(response) {
    for (let i = 0; i < response.length; i++) {
        fetchMessages(response[i])
    }

}


function createMessage(response) {
    console.log(response)
    let src
    response.sender.avatar === null ? src = 'https://bootdey.com/img/Content/avatar/avatar6.png' : src = '\\image\\' + response.sender.avatar.id
    const message = document.createElement('div')
    message.id = "message-" + response.id
    const date = new Date(response.sendTime);
    // const year = date.getFullYear();
    // const month = date.getMonth();
    // const day = date.getDay();

    const hour = date.getHours();
    const minutes = date.getMinutes();

    const dateString = `${hour}:${minutes}`;
    if (response.sender.username !== user.username) {
        message.classList.add("d-flex", "flex-row", "justify-content-start","rounded-3","mt-3","mb-3")
        message.innerHTML = "    <img class=\"rounded-circle\" src=\"" + src + "\"\n" +
            "         alt=\"avatar 1\" style=\"width: 45px; height: 45px;\"  data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\" title=\""+response.sender.name+" " +response.sender.surname+"\">\n" +
            "        <div>\n" +
            "            <p class=\"small p-2 ms-3 mb-1 rounded-3\" style=\"background-color: #f5f6f7;\">" + response.content + " </p>\n" +
            "            <p class=\"small ms-3 mb-3 rounded-3 text-muted float-start\">" + dateString + "</p>\n" +
            "        </div>"
    } else {

        message.classList.add("d-flex", "flex-row", "justify-content-end","rounded-3","mt-3","mb-3")
        message.innerHTML = "<div>\n" +
            "        <p class=\"small p-2 me-3 mb-1 text-white rounded-3 bg-primary\">" + response.content + "</p>\n" +
            "        <span class=\"small me-3  rounded-3 text-muted float-end\">" + dateString + "</span>\n" +
            "    </div>\n" +
            "    <img class=\"rounded-circle\" src=\"" + src + "\"\n" +
            "         alt=\"avatar 1\" style=\"width: 45px; height: 45px;\">"
        message.addEventListener('click',function ()
        {
            message.classList.toggle('active-message')
        })
    }

    return message

    // chatArea.scrollTop = chatArea.scrollHeight
}

function fetchMessages(message) {
    chatArea.insertBefore(createMessage(message), chatArea.firstChild)
}


function connect() {
    if (currentRoom) {
        const socket = new SockJS('/chat')
        stompClient = Stomp.over(socket)
        stompClient.connect({}, onConnected, onError)
    }
}

const onConnected = () => {

    stompClient.subscribe('/topic/' + currentRoom.id, onMessageReceived, {id: '/topic/' + currentRoom.id})

}

const onError = (error) => {
    console.log(error)
}

const sendMessage = () => {
    const messageInput = document.querySelector('#exampleFormControlInput2')
    const messageContent = messageInput.value.trim();
    if (messageContent === '') {
        return
    }
    const chatMessage = {
        content: messageContent,
        sender: user,
        sendTime: new Date(),
        chatRoom: currentRoom

    }
    if (messageContent && stompClient)

        stompClient.send("/chat.send/" + currentRoom.id, {}, JSON.stringify(chatMessage))
    messageInput.value = ''

}


const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body)
    chatArea.appendChild(createMessage(message))
}

sendButton.addEventListener('click', sendMessage)
document.querySelector('#exampleFormControlInput2').addEventListener("keydown", function (event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        sendMessage();
    }
});

function customizeChatArea() {
    let roomName = document.querySelector('.card-title')
    let cancelButton = document.querySelector('.btn-secondary')
    cancelButton.addEventListener('click', backToMenu)
    let infoButton = document.querySelector('.btn-white')
    roomName.addEventListener('click', showInfoAboutRoom)
    roomName.innerHTML = currentRoom.roomName




}

function showInfoAboutRoom() {
    var settings = {
        "url": "http://localhost:8080/users/search/showInfo?id=" + currentRoom.id,
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
    };
    $.ajax(settings).done(function (response) {
        usersInRoom.innerHTML=''
        let allUsers = response._embedded.users
        for (let i = 0; i < allUsers.length; i++) {
            createUser(allUsers[i])
        }
    });
}

function backToMenu() {
    if (currentRoom && stompClient) {
        stompClient.unsubscribe('/topic/' + currentRoom.id)
    }
    currentRoom = null
    topChatArea.style.display = 'none'
    chatArea.style.display = 'none'
    chatInputArea.style.display = 'none'
    chatInputArea.classList.remove('d-flex')
    noChatArea.style.display = 'block'

}


function createUser(response) {
    let src
    response.avatar === null ? src = 'https://bootdey.com/img/Content/avatar/avatar6.png' : src = '\\image\\' + response.avatar.id
    let user = document.createElement('li')
    user.classList.add("p-2", "border-bottom")
    user.innerHTML = " <a href=\"#!\" class=\"d-flex justify-content-between\">   <div class=\"d-flex align-items-center flex-row\">\n" +
        "        <div>\n" +
        "            <img src=\" " + src + " \" alt=\"avatar\"\n" +
        "                 class=\"d-flex align-self-center me-3 rounded-circle\" style='width: 45px; height: 45px;'>\n" +
        "        </div>\n" +
        "        <div class=\"pt-1\">\n" +
        "            <p class=\"fw-bold mb-0\">" + response.name + " " + response.surname + "</p>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</a>"
    usersInRoom.appendChild(user)
}

// <li className="p-2 border-bottom"><a href="#!" className="d-flex justify-content-between">
//     <div className="d-flex flex-row">
//         <div>
//             <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp" alt="avatar"
//                  className="d-flex align-self-center me-3" width="60">
//                 <span className="badge bg-success badge-dot"></span>
//         </div>
//         <div className="pt-1">
//             <p className="fw-bold mb-0">Комната для гениев</p>
//             <p className="small text-muted">Move to this chat</p>
//         </div>
//     </div>
//     <div className="pt-1">
//         <p className="small text-muted mb-1">Just now</p>
//     </div>
// </a></li>

$(chatArea).scroll(function () {

    if (chatArea.getBoundingClientRect().y - chatArea.firstChild.getBoundingClientRect().y === -16 && lastResponse._links.self.href !== lastResponse._links.last.href) {
        let lastScrollHeight = chatArea.scrollHeight


        var settings = {
            "url": lastResponse._links.next.href,
            "method": "GET",
            "timeout": 0,
            "headers": {
                "Content-Type": "application/json"
            },
        };
        $.ajax(settings).done(function (response) {
            console.log(response)
            lastResponse = response
            showAllMessages(response._embedded.messages)
            let scrollDif = chatArea.scrollHeight - lastScrollHeight
            chatArea.scrollTop += scrollDif
        });
    }
})

