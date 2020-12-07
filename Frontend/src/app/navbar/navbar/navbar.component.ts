import { Component, OnInit, OnChanges, OnDestroy, AfterContentInit, ChangeDetectorRef, ElementRef, Renderer2, QueryList, SimpleChanges } from '@angular/core';
import { Router, RouterLink, RouterLinkWithHref, Routes, RouterModule } from '@angular/router';
import { AppRoutingModule } from '../../app-routing.module';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(private router:AppRoutingModule) {
    
  }

}