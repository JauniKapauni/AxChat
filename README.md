# AxChat
>Minecraft plugin to enable and manage cross-server chat
---
## Requirements
- Minecraft Paper 26.1.2 Server
- MariaDB / MySQL Server
- Redis Server
- PlaceholderAPI
---
## Features
- Cross-server chat
  - Global
  - Private
- Customize Join/Quit Message
---
## Installation
1. Download the latest release
2. Put the `.jar` into the `/plugins` folder of every server to be synchronized
3. Start the server
4. Stop the server
5. Edit the `config.yml`
6. Start the server
---
## Configuration
```
# config.yml
redis:
  host:
  port:
forbidden-words:
  - "badword1"
  - "badword2"
  - "badlink"
join_message_enabled: false
quit_message_enabled: false
join_message: "is online!"
quit_message: "is offline!"
chat_cooldown: 5000
```
---
## Commands & Permissions
- `/msg` - `axchat.msg`
- `/reload` - `axchat.reload`
- `/mail` - `axchat.mail`
---
## Support
Discord: [AxForge](https://discord.gg/rYSxV4daS8)

---
## License
AxChat is licensed under the permissive MIT License. Please see [LICENSE](https://github.com/JauniKapauni/AxChat/blob/master/LICENSE) for more info.