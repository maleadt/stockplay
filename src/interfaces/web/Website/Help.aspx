<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Help.aspx.cs" Inherits="Help" Title="Help" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder" Runat="Server">
    <h1>Help</h1>
    
    <p>Welkom bij de Stockplay helppagina. Hier kan je algemene informatie vinden over het spel en ook de status
    van het project bekijken.</p>
    
    <h2>Over Stockplay</h2>
    <p class="para">Stockplay is een beursspel waar je fictieve aandelen aankoopt. De beurs van Stockplay omvat verschillende
    beursen van de hele wereld en werkt met live koersen, rechtstreeks van de beurs.</p>
    <p class="para">Om mee te spelen kan je een account registreren op de <a runat="server" href="~/Register.aspx">registratie pagina</a>.
    Je hoeft enkel je naam en e-mail op te geven om te beginnen spelen. Vervolgens krijg je een startbedrag toegewezen
    waarmee je kunt kopen op de beurs. Voor een overzicht van de beschikbare effecten ga je naar het 
    <a runat="server" href="SecuritiesOverview.aspx">effectenoverzicht.</a></p>
    <p class="para">Eenmaal je een aandeel gevonden hebt dat je wilt kopen, klik je op de 'Buy Security' knop om naar de 
    orderpagina te gaan. Hier kan je opgeven hoeveel aandelen je wilt kopen. Momenteel ondersteunt Stockplay enkel
    gewone orders die onmiddellijk uitgevoerd worden, maar in de toekomst zal je complexere orders kunnen plaatsen.</p>
    
    <h2>Status</h2>
    <p class="para">Het Stockplay project is nog in volle ontwikkeling en bevat nog niet alle functionaliteit die in het
    eindproduct aanwezig zal zijn. Hieronder kun je de belangrijkste punten vinden die nog moeten afgewerkt worden:</p>
    <ul>
        <li><b>Mobiele client</b>: Je zal je portfolio kunnen bekijken en beheren vanuit een Java applicatie op je gsm.</li>
        <li><b>Meertalige website</b>: De website is nu enkel beschikbaar in het Engels, maar deze zal in de toekomst
        meertalig worden</li>
        <li><b>Complexe orders</b>: Je zal uitgebreide regels kunnen instellen om aandelen automatische te kopen
        of verkopen.</li>
        <li><b>Ranking</b>: Een ranking pagina waar de beste beleggers in terecht komen.</li>
    </ul>
</asp:Content>

