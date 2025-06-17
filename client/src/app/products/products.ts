import { Component } from '@angular/core';
import { Product } from '../product';
import { Auth } from '../auth';
import { Router } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-products',
  imports: [
    CommonModule,
    MatInputModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSortModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule
  ],
  templateUrl: './products.html',
  styleUrl: './products.scss'
})
export class Products {
  data: Product[] = [];
  displayedColumns: string[] = ['prodName', 'prodDesc', 'prodPrice'];
  isLoadingResults = true;

  constructor(private productService: Product, private authService: Auth, private router: Router) { }

  ngOnInit() {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe({
        next: (products) => {
          this.data = products;
          console.log(this.data);
          this.isLoadingResults = false;
        },
        error: (err) => {
          console.log(err);
          this.isLoadingResults = false;
        }
      });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['login']);
  }
}
