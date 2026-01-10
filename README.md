<center><img src="https://i.postimg.cc/MKPVVR1s/dplogo-512.png" alt="logo"></center>
<center><img src="https://i.postimg.cc/RZ9dqPFx/introduce.png" alt="introduce"></center>

Example Video: *Coming soon!*

This plugin introduces an **item appraisal** system to your Minecraft server. Players can submit certain items to be **evaluated (appraised)** and receive a random in-game value in return.  
They can then sell those appraised items for currency, adding a fun, luck-based way to earn rewards beyond standard gameplay – all without complicated setup!

---

<center><img src="https://i.postimg.cc/RZ9dqP08/description.png" alt="description"></center>

- Define a list of **appraisable items** and set a custom **value range** for each  
- Players can **appraise items** to reveal a random value within the configured range  
- Appraised items are tagged with their **appraised value** in item lore  
- Players can **sell appraised items** to instantly receive in-game money  
- Intuitive **GUI menus** for item evaluation and selling  

---

<center><img src="https://i.postimg.cc/rwcjzhpH/depend-plugin.png" alt="depend-plugin"></center>

- All DP-Plugins require the **`DPP-Core`** plugin  
- The plugin will not work if **`DPP-Core`** is not installed  
- You can download **`DPP-Core`** here: <a href="https://github.com/DP-Plugins/DPP-Core/releases" target="_blank">Click me!</a>  

---

<center><img src="https://i.postimg.cc/dV01RxJB/installation.png" alt="installation"></center>

1️⃣ Place the **`DPP-Core`** plugin and this plugin file (**`DP-Evaluation-*.jar`**) into your server’s **`plugins`** folder  

2️⃣ Restart the server, and the plugin will be automatically enabled  

3️⃣ If needed, you can open and modify **`config.yml`** and **`plugin.yml`** to customize settings  

---

<center><img src="https://i.postimg.cc/jSKcC85K/settings.png" alt="settings"></center>

- **`config.yml`**: Manages basic plugin settings and stores appraisable items with their price ranges  

---

<center><img src="https://i.postimg.cc/SxqdjZKw/command.png" alt="command"></center>

❗ Some commands require admin permission (`dpev.admin`)

**Command List and Examples**

| Command | Permission | Description | Example |
|------|------------|-------------|---------|
| `/dpev reload` | dpev.admin | Reload plugin configuration | `/dpev reload` |
| `/dpev items` | dpev.admin | Configure appraisable items | `/dpev items` |
| `/dpev price` | dpev.admin | Set item appraisal price ranges | `/dpev price` |
| `/dpev evaluate` | None | Open item evaluation GUI | `/dpev evaluate` |
| `/dpev sell` | None | Open selling GUI for appraised items | `/dpev sell` |

**❗Notes when using commands**

- Only items configured by admins can be appraised  
- Price ranges are entered in chat as `min-max` (example: `100-500`)  
- Items must be **appraised before selling**  
- GUI safely returns items if closed prematurely  
- Admin commands require **OP** or `dpev.admin` permission  

---

<center><img src="https://i.postimg.cc/Z5ZH0fqL/api-integration.png" alt="api-integration"></center>

- Integrates with the EssentialsX's economy system to deposit money when selling items  

---

<center><a href="https://discord.gg/JnMCqkn2FX"><img src="https://i.postimg.cc/4xZPn8dC/discord.png" alt="discord"></a></center>

- https://discord.gg/JnMCqkn2FX  
- For questions, bug reports, or feature suggestions, please join our Discord  
- Feedback and improvement ideas are always welcome!

---
